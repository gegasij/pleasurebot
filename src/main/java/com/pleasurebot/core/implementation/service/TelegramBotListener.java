package com.pleasurebot.core.implementation.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotListener {
    public TelegramBotListener(@Qualifier("implementation") TelegramBot telegramBot, UpdateResolver updateResolver) {
        telegramBot.setUpdatesListener(updates -> {
            long startTime = System.currentTimeMillis();
            Update update = updates.get(0);
            try {
                updateResolver.resolveUpdate(update);
            } catch (Exception exception) {
                System.out.println(exception);
            }
            System.out.println("time processing " + ((float) (System.currentTimeMillis() - startTime)) / 1000);
            return update.updateId();
            //return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
