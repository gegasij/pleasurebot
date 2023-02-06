package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pleasurebot.core.model.Attachment;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.service.utils.MediaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TelegramBotApi {
    private final TelegramBot telegramBot;

    public BaseResponse sendMessage(BaseRequest<?, ?> editMessageText) {
        return telegramBot.execute(editMessageText);
    }

    public BaseResponse executeEditMessage(TelegramMenuMessage customEditMenuMessage, Long editMessageId, Long chatId) {
        if (customEditMenuMessage.getAttachments() != null && !customEditMenuMessage.getAttachments().isEmpty()) {
            return deleteAndSend(customEditMenuMessage, editMessageId.intValue(), chatId);
        }
        InlineKeyboardMarkup keyboardMarkup = null;
        if (customEditMenuMessage.getInlineKeyboard() != null) {
            keyboardMarkup = new InlineKeyboardMarkup();
            Arrays.stream(customEditMenuMessage.getInlineKeyboard()).forEach(keyboardMarkup::addRow);
        }
        EditMessageText editMessageText = new EditMessageText(chatId,
                editMessageId.intValue(),
                customEditMenuMessage.getMessage())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        if (keyboardMarkup != null) {
            editMessageText.replyMarkup(keyboardMarkup);
        }
        return sendMessage(editMessageText);

    }

    public void executeSendMessage(TelegramMenuMessage editMenuMessage, Long chatId) {
        AbstractSendRequest<?> request;
        if (editMenuMessage.getAttachments() != null && !editMenuMessage.getAttachments().isEmpty()) {
            Attachment s = editMenuMessage.getAttachments().stream().findFirst().get();
            request = MediaUtil.getMediaMessage(s, chatId, editMenuMessage.getMessage());
        } else {
            request = new SendMessage(chatId,
                    editMenuMessage.getMessage())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true);
        }
        if (editMenuMessage.getInlineKeyboard() != null && request != null) {
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            Arrays.stream(editMenuMessage.getInlineKeyboard()).forEach(keyboardMarkup::addRow);
            request.replyMarkup(keyboardMarkup);
        }
        sendMessage(request);
    }

    public BaseResponse deleteAndSend(TelegramMenuMessage telegramMenuMessage, Integer messageId, Long chatId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        executeSendMessage(telegramMenuMessage, chatId);
        return sendMessage(deleteMessage);
    }
}
