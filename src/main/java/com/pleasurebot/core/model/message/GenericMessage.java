package com.pleasurebot.core.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class GenericMessage {
    private String message;
    private Long chatId;
}
