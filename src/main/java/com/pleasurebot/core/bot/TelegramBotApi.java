package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pleasurebot.core.model.message.CustomEditMenuMessage;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.model.message.MenuMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBotApi {
    private final TelegramBot telegramBot;

    public void sendMessage(BaseRequest<?, ?> editMessageText) {
        telegramBot.execute(editMessageText);
    }

    public void sendEditMenuMessage(EditMenuMessage editMenuMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtons = editMenuMessage.getMenuList()
                .stream()
                .map(it -> new InlineKeyboardButton(it.getFirst()).callbackData(it.getSecond()))
                .toList();

        keyboardMarkup.addRow(inlineKeyboardButtons.toArray(new InlineKeyboardButton[0]));

        EditMessageText editMessageText = new EditMessageText(editMenuMessage.getChatId(),
                editMenuMessage.getEditedMessageId().intValue(),
                editMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(keyboardMarkup);

        sendMessage(editMessageText);
    }

    public void sendEditMenuMessage(CustomEditMenuMessage customEditMenuMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        Arrays.stream(customEditMenuMessage.getInlineKeyboardButtons()).forEach(keyboardMarkup::addRow);

        EditMessageText editMessageText = new EditMessageText(customEditMenuMessage.getChatId(),
                customEditMenuMessage.getEditedMessageId().intValue(),
                customEditMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(keyboardMarkup);

        sendMessage(editMessageText);
    }

    public void sendMenuMessage(MenuMessage editMenuMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtons = editMenuMessage.getMenuList()
                .stream()
                .map(it -> new InlineKeyboardButton(it.getFirst()).callbackData(it.getSecond()))
                .toList();

        keyboardMarkup.addRow(inlineKeyboardButtons.toArray(new InlineKeyboardButton[0]));

        SendMessage sendMessage = new SendMessage(editMenuMessage.getChatId(),
                editMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }
}
