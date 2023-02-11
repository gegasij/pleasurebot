package com.pleasurebot.core.bot.model;

import lombok.Getter;

@Getter
public enum BotState {
    NONE(0), ADD_BUNDLE(1), CHANGE_CONSUMER_LOGIN(2);

    private final int value;

    BotState(int i) {
        this.value = i;
    }
}
