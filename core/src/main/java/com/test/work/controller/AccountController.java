package main.java.com.test.work.controller;

import com.test.work.api.AccountApi;
import com.test.work.exception.ErrorResponseException;
import com.test.work.exception.ExceptionService;
import com.test.work.model.TransferRequest;
import com.test.work.model.TransferResponse;
import com.test.work.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController implements AccountApi {

    private final AccountService accountService;

    private final ExceptionService exceptionService;

    @Override
    public ResponseEntity<TransferResponse> transferMoney(TransferRequest transferRequest) {
        TransferResponse response;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long fromUserId = Long.valueOf(authentication.getName());

        try {
            response = accountService.transferMoney(fromUserId, transferRequest);
            log.info("transfer money {}", response);
        } catch (ErrorResponseException e) {
            throw exceptionService.generateResultCodeException(e);
        }

        return ResponseEntity.ok(response);
    }
}
