package com.pleasurebot.core.model.message;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class CustomEditMenuMessage extends GenericEditMessage {
    private InlineKeyboardButton[][] inlineKeyboardButtons;
}
