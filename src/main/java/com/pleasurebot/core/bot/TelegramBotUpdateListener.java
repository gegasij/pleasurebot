package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotUpdateListener {
    private final TelegramUpdateHandler telegramUpdateHandler;
    private final TelegramBot telegramBot;

    public TelegramBotUpdateListener(TelegramBot telegramBot, TelegramUpdateHandler updateHandler) {
        telegramBot.setUpdatesListener(updates -> {
            long startTime = System.currentTimeMillis();
            Update update = updates.get(0);
            updateHandler.handleUpdate(update);
            System.out.println("time processing " + ((float)(System.currentTimeMillis() - startTime))/1000);
            return update.updateId();
            //return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        this.telegramBot = telegramBot;
        this.telegramUpdateHandler = updateHandler;
    }

}
