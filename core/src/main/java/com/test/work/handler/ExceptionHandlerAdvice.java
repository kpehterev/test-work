package com.test.work.handler;

import com.test.work.exception.ResultCodeException;
import com.test.work.i18n.MsgSourceService;
import com.test.work.model.BackendErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.security.sasl.AuthenticationException;

@Slf4j
@ControllerAdvice(basePackages = "com.test.work.controller")
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

    private final MsgSourceService msgSourceService;
    @ExceptionHandler(ResultCodeException.class)
    public ResponseEntity<BackendErrorResponse> handleException(ResultCodeException e) {
        log.error(e.getErrorResponse().getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getErrorResponse());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>("The requested method is not supported.", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtExceptionException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BackendErrorResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("methodArgumentTypeMismatchException", e);
        var title = msgSourceService.getTitle("methodArgumentTypeMismatchException");
        var message = msgSourceService.getErrorMessage("methodArgumentTypeMismatchException");
        var body = new BackendErrorResponse()
                .title(title)
                .message(message)
                .errors(null);

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(body);
    }

}
