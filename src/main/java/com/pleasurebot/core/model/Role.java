package com.pleasurebot.core.model;

import lombok.Getter;

public enum Role {
    ANONYMOUS(0), CLIENT(1), CONSUMER(2);
    @Getter
    private final Integer roleId;

    Role(int i) {
        roleId = i;
    }
}
