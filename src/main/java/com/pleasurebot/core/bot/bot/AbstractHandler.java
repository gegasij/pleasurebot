package com.pleasurebot.core.bot.bot;

import com.pengrad.telegrambot.model.Update;

public interface AbstractHandler {
    Object handleUpdate(Update update);
}
