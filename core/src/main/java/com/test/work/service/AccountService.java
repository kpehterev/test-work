package main.java.com.test.work.service;

import com.test.work.entity.AccountEntity;
import com.test.work.entity.UserEntity;
import com.test.work.exception.ErrorResponseException;
import com.test.work.model.TransferRequest;
import com.test.work.model.TransferResponse;
import com.test.work.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.test.work.exception.exceptionfactory.NotFoundExceptionFactory.generateUserNotFoundException;

@RequiredArgsConstructor
@Service
public class AccountService extends AbstractService<AccountRepository, AccountEntity, Long> {

    private final AccountRepository repository;

    private final UserService userService;

    @Override
    protected AccountRepository getRepository() {
        return this.repository;
    }

    public void createAndSaveAccount(UserEntity user, Double initialBalance) {
        AccountEntity account = new AccountEntity();
        account.setBalance(new BigDecimal(initialBalance));
        account.setUser(user);
        user.setAccount(account);
        save(account);
    }

    @Transactional
    public TransferResponse transferMoney(Long fromUserId, TransferRequest request) throws ErrorResponseException {

        BigDecimal value = new BigDecimal(request.getValue());
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }

        UserEntity fromUser = userService.findById(fromUserId)
                .orElseThrow(() -> generateUserNotFoundException(fromUserId));
        UserEntity toUser = userService.findById(request.getToUserId())
                .orElseThrow(() -> generateUserNotFoundException(request.getToUserId()));

        if (fromUser.getAccount().getBalance().compareTo(value) < 0) {
            throw new RuntimeException("Недостаточно средств на счете");
        }

        BigDecimal newBalanceFrom = fromUser.getAccount().getBalance().subtract(value);
        BigDecimal newBalanceTo = toUser.getAccount().getBalance().add(value);

        fromUser.getAccount().setBalance(newBalanceFrom);
        toUser.getAccount().setBalance(newBalanceTo);

        userService.save(fromUser);
        userService.save(toUser);

        return new TransferResponse(
                fromUserId,
                request.getToUserId(),
                value.doubleValue(),
                newBalanceFrom.doubleValue(),
                newBalanceTo.doubleValue()
        );
    }
}
