package main.java.com.test.work.exception.exceptionfactory;

import com.test.work.exception.ErrorResponseException;

import java.util.List;

import static com.test.work.exception.ExceptionService.buildLocaleBackendError;

public class NotFoundExceptionFactory {

    public static ErrorResponseException generateUserNotFoundException(Long userId) {
        return new ErrorResponseException(buildLocaleBackendError("generateUserNotFoundException", List.of(userId)));
    }

}