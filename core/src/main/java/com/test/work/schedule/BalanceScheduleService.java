package com.test.work.schedule;

import com.test.work.entity.AccountEntity;
import com.test.work.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceScheduleService {

    private final AccountService accountService;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void increaseBalances() {
        List<AccountEntity> accounts = accountService.findAll();

        for (AccountEntity account : accounts) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal initialDeposit = account.getBalance();
            BigDecimal maxAllowedBalance = initialDeposit.multiply(new BigDecimal("2.07"));

            if (currentBalance.compareTo(maxAllowedBalance) < 0) {
                BigDecimal newBalance = currentBalance.multiply(new BigDecimal("1.10"));
                if (newBalance.compareTo(maxAllowedBalance) > 0) {
                    newBalance = maxAllowedBalance;
                }
                account.setBalance(newBalance);
                accountService.save(account);

                log.info("Updated balance for account ID {}: {} -> {}", account.getId(), currentBalance, newBalance);
            }
        }
    }
}
