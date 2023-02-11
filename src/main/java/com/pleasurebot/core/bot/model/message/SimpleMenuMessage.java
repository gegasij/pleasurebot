package com.pleasurebot.core.bot.model.message;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class SimpleMenuMessage extends TelegramMenuMessage {
    private List<Pair<String, String>> menuList;

    @Override
    public InlineKeyboardButton[][] getInlineKeyboard() {
        if (menuList == null) {
            return null;
        }
        InlineKeyboardButton[] inlineKeyboardButtons = menuList.stream()
                .map(it -> new InlineKeyboardButton(it.getFirst()).callbackData(it.getSecond()))
                .toList()
                .toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardButton[][]{inlineKeyboardButtons};
    }
}
