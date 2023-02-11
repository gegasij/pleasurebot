package com.pleasurebot.core.bot.model.message;

import com.pleasurebot.core.bot.model.Attachment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public abstract class GenericMessage {
    private String message;
    private List<Attachment> attachments;
}
