package com.pleasurebot.core.model.message;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.util.Pair;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class CustomTelegramMenuMessage extends TelegramMenuMessage {
    private List<List<Pair<String, String>>> inlineKeyboardButtons;

    @Override
    public InlineKeyboardButton[][] getInlineKeyboard() {
        return inlineKeyboardButtons == null ?
                null : inlineKeyboardButtons.stream()
                .map(it -> it.stream()
                        .map(it2 -> new InlineKeyboardButton(it2.getFirst()).callbackData(it2.getSecond()))
                        .toList()
                )
                .map(it -> it.toArray(new InlineKeyboardButton[0]))
                .toList()
                .toArray(new InlineKeyboardButton[0][]);
    }
}
