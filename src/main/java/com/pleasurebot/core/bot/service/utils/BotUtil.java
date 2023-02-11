package com.pleasurebot.core.bot.service.utils;

import com.pengrad.telegrambot.model.Update;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BotUtil {
    public static Long getChatId(Update update) {
        if (update.callbackQuery() != null) {
            if (update.callbackQuery().message() != null) {
                return update.callbackQuery().message().chat().id();
            }
        } else if (update.message() != null) {
            return update.message().chat().id();
        }
        throw new RuntimeException("cant find chatID " + update);
    }

    public static String getTelegramUsername(Update update) {
        if (update.callbackQuery() != null) {
            if (update.callbackQuery().message() != null) {
                return update.callbackQuery().message().chat().username();
            }
        } else if (update.message() != null) {
            return update.message().chat().username();
        }
        throw new RuntimeException("cant find chatID " + update);
    }

    public static String getCommand(Update update) {
        String command = null;
        if (update.callbackQuery() != null) {
            command = CallbackDataParser.getCommand(update.callbackQuery().data());
        }
        if (update.message() != null && update.message().text() != null) {
            command = CallbackDataParser.getCommand(update.message().text());
            if (command != null) {
                if (command.charAt(0) == '/') {
                    command = command.replaceFirst("/", "");
                }
            }
        }
        return command;
    }

    public static String getTextOrCaption(Update update) {
        if (update.message() != null) {
            if (update.message().text() != null) {
                return update.message().text();
            }
            if (update.message().caption() != null) {
                return update.message().caption();
            }
        }
        return null;
    }

}
