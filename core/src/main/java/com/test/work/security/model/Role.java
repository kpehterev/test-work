package com.test.work.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"), NF("NF"), RUSINOX("RUSINOX"), CARBO("CARBO"), IDC("IDC");

    private final String value;
}
