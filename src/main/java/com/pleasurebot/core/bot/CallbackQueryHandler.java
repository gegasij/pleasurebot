package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.bot.handler.HandlerFabric;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements AbstractHandler {
    private final HandlerFabric handlerFabric;

    @Override
    public TelegramMenuMessage handleUpdate(Update update) {
        if (update.callbackQuery() != null && update.callbackQuery().data() != null) {
            CommandHandler handler = handlerFabric.getHandler(update.callbackQuery().data());
            if (handler != null) {
                return handler.handleCommand(update);
            }
        }
        return null;
    }
}
