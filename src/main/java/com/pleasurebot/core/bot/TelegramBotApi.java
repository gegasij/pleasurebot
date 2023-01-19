package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pleasurebot.core.model.EditMenuMessage;
import com.pleasurebot.core.model.SendMenuMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramBotApi {
    private final TelegramBot telegramBot;

    public void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    public void sendEditMessage(EditMessageText editMessageText) {
        telegramBot.execute(editMessageText);
    }

    public void executeEditMenuMessage(EditMenuMessage editMenuMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        editMenuMessage.getMenuList()
                .stream()
                .map(it -> new InlineKeyboardButton(it.getFirst()).callbackData(it.getSecond()))
                .forEach(keyboardMarkup::addRow);

        EditMessageText editMessageText = new EditMessageText(editMenuMessage.getChatId(),
                editMenuMessage.getMessageId().intValue(),
                editMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(keyboardMarkup);

        sendEditMessage(editMessageText);
    }

    public void executeSendMenuMessage(SendMenuMessage editMenuMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        editMenuMessage.getMenuList()
                .stream()
                .map(it -> new InlineKeyboardButton(it.getFirst()).callbackData(it.getSecond()))
                .forEach(keyboardMarkup::addRow);

        SendMessage sendMessage = new SendMessage(editMenuMessage.getChatId(),
                editMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }
}
