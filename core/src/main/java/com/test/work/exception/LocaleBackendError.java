package com.test.work.exception;

import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Builder
@Getter
public class LocaleBackendError {

    @NonNull
    private final String key;
    @Nullable
    private final List<Object> args;
}
