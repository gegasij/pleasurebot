package com.pleasurebot.core.bot.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.model.message.TelegramMenuMessage;

public interface CommandHandler {
     TelegramMenuMessage handleCommand(Update update);
     Boolean checkAccessToCommand(Update update);

     String getCommand();
}
