package main.java.com.test.work.exception;

import com.test.work.exception.ErrorResponseException;
import com.test.work.exception.LocaleBackendError;
import com.test.work.exception.ResultCodeException;
import com.test.work.i18n.MsgSourceService;
import io.micrometer.common.lang.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    private final MsgSourceService msgSourceService;

    public ResultCodeException generateResultCodeException(@NonNull ErrorResponseException error){
        var title = msgSourceService.getTitle(error.getKey());
        var status = error.getKey().contains("NotFound") ? HttpStatus.NOT_FOUND : HttpStatus.UNPROCESSABLE_ENTITY;
        var args = error.getArgs() != null
                && !error.getArgs().isEmpty()
                && error.getArgs().stream().anyMatch(Objects::nonNull)
                ? error.getArgs()
                : null;
        var message = msgSourceService.getErrorMessage(error.getKey(), args != null ? args.toArray() : null);
        return new ResultCodeException(status, title, message);
    }

    public static LocaleBackendError buildLocaleBackendError(@NonNull String key, @Nullable List<Object> args){
        return LocaleBackendError.builder()
                .key(key)
                .args(args)
                .build();
    }

    public static <T> List<T> convertOptional(T arg) {
        return arg == null ? Collections.emptyList() : List.of(arg);
    }

}