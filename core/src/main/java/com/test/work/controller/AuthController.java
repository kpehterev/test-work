package main.java.com.test.work.controller;

import com.test.work.api.AuthorizationApi;
import com.test.work.exception.ErrorResponseException;
import com.test.work.exception.ExceptionService;
import com.test.work.model.UserCredits;
import com.test.work.model.UserTokens;
import com.test.work.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthorizationApi {

    private final AuthService authService;
    private final ExceptionService exception;

    @Override
    public ResponseEntity<UserTokens> login(UserCredits userCredits) {
        try {
            return ResponseEntity.ok(authService.authenticate(userCredits));
        }catch (ErrorResponseException e){
            throw exception.generateResultCodeException(e);
        }
    }

    @Override
    public ResponseEntity<UserTokens> refresh(UserTokens userTokens) {
        try {
            return ResponseEntity.ok(authService.refreshToken(userTokens));
        }catch (ErrorResponseException e){
            throw exception.generateResultCodeException(e);
        }
    }
}