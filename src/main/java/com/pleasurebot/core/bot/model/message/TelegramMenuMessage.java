package com.pleasurebot.core.bot.model.message;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class TelegramMenuMessage extends GenericMessage {
    public abstract InlineKeyboardButton[][] getInlineKeyboard();
}
