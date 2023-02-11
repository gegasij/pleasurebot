package com.pleasurebot.core.bot.service.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CallbackDataParser {
    private static final int COMMAND_POSITION = 1;
    private static final int PARAMETER1_POSITION = 2;

    public static String getCommand(String callbackData) {
        if (callbackData == null) {
            return null;
        }
        String[] split = callbackData.split(" ");
        if (split.length >= COMMAND_POSITION) {
            return split[COMMAND_POSITION - 1];
        }
        return null;
    }

    public static String getParameter1(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= PARAMETER1_POSITION) {
            return split[PARAMETER1_POSITION - 1];
        }
        return null;
    }
}
