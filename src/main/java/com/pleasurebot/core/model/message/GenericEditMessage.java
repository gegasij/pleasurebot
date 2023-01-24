package com.pleasurebot.core.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class GenericEditMessage extends GenericMessage {
    private Long editedMessageId;
}
