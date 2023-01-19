package com.pleasurebot.core.service;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CallbackDataParser {
    private static final int ROLE_POSITION = 1;
    private static final int START_ADMIN_COMMAND = 2;
    private static final int START_CLIENT_COMMAND = 2;
    private static final int START_CONSUMER_COMMAND = 2;

    public static String parseRoleCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= ROLE_POSITION) {
            return split[ROLE_POSITION - 1];
        }
        return null;
    }
    public static String parseStartAdminCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_ADMIN_COMMAND) {
            return split[START_ADMIN_COMMAND - 1];
        }
        return null;
    }
    public static String parseStartClientCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_CLIENT_COMMAND) {
            return split[START_CLIENT_COMMAND - 1];
        }
        return null;
    }
    public static String parseStartConsumerCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_CONSUMER_COMMAND) {
            return split[START_CONSUMER_COMMAND - 1];
        }
        return null;
    }
}
