package com.test.work.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.Collection;

@Getter
public class ErrorResponseException extends Exception {
    @Serial
    private static final long serialVersionUID = -6082888094400622524L;

    private final LocaleBackendError errorResponse;
    private final String key;
    private final Collection<Object> args;

    public ErrorResponseException(LocaleBackendError errorResponse) {
        this.errorResponse = errorResponse;
        this.key = errorResponse.getKey();
        this.args = errorResponse.getArgs();
    }
}
