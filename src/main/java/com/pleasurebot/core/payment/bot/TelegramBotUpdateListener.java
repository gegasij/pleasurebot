package com.pleasurebot.core.payment.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "activatorBotListener")
public class TelegramBotUpdateListener {

    public TelegramBotUpdateListener(@Qualifier("activatorBot") TelegramBot telegramBot, UpdateResolver updateResolver, TelegramBotApi telegramBotApi) {
        telegramBot.setUpdatesListener(updates -> {
            long startTime = System.currentTimeMillis();
            Update update = updates.get(0);
            try{
                updateResolver.resolveUpdate(update);
            } catch (Exception exception){
                System.out.println(exception);
            }
            System.out.println("time processing " + ((float) (System.currentTimeMillis() - startTime)) / 1000);
            return update.updateId();
            //return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
