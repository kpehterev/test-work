package com.test.work.util;

import com.test.work.exception.ErrorResponseException;
import io.micrometer.common.lang.Nullable;
import lombok.Getter;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
public abstract class AbstractEntityUtil<T> {

    private final UUID USER_ID = UUID.fromString("9fd9f480-b944-11ee-813b-2b05bebdd007");

    protected abstract T create(@Nullable Consumer<T> body) throws ErrorResponseException;

}
