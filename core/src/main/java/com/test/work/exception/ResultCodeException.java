package com.test.work.exception;

import com.test.work.model.BackendErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Slf4j
@Getter
public class ResultCodeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8926201816988238810L;

    private final HttpStatus httpStatus;
    private final BackendErrorResponse errorResponse;

    public ResultCodeException(HttpStatus httpStatus, String title, String message) {
        this.httpStatus = httpStatus;
        this.errorResponse = new BackendErrorResponse()
                .title(title)
                .message(message)
                .errors(null);
    }
}