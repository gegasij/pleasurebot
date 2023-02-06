package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.utils.MediaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateResolver {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final TelegramBotApi telegramBotApi;

    public Boolean resolveUpdate(Update update) {
        if (update.callbackQuery() != null) {
            TelegramMenuMessage telegramMenuMessage = callbackQueryHandler.handleUpdate(update);
            if (telegramMenuMessage != null) {
                Long chatId = BotUtil.getChatId(update);
                Integer messageId = update.callbackQuery().message().messageId();
                if (MediaUtil.isAnyMedia(update)) {
                    telegramBotApi.deleteAndSend(telegramMenuMessage, messageId, chatId);
                } else telegramBotApi.executeEditMessage(telegramMenuMessage, messageId.longValue(), chatId);
                return true;
            }
        } else if (update.message() != null) {
            if (update.message().chat().id() != null && update.message().messageId() != null) {
                TelegramMenuMessage telegramMenuMessage = messageHandler.handleUpdate(update);
                if (telegramMenuMessage != null) {
                    Long chatId = BotUtil.getChatId(update);
                    telegramBotApi.executeSendMessage(telegramMenuMessage, chatId);
                    return true;
                }
            }
        }
        return false;
    }
}
