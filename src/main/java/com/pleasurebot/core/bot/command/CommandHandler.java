package com.pleasurebot.core.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.model.message.TelegramMenuMessage;

public interface CommandHandler {
     TelegramMenuMessage handleCommand(Update update);
     Boolean checkAccessToCommand(Update update);

     String getCommand();
}
