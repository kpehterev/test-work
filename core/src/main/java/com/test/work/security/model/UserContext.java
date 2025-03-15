package com.test.work.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserContext {

    private String name;
    private String password;
    private Role role;
}
