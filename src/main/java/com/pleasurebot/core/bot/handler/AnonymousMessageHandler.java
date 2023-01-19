package com.pleasurebot.core.bot.handler;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class AnonymousMessageHandler {
    public Boolean handleUpdate(Update update) {
return false;
    }
}
