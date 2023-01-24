package com.pleasurebot.core.service;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CallbackDataParser {
    private static final int ROLE_POSITION = 1;
    private static final int START_ADMIN_COMMAND = 2;
    private static final int START_CLIENT_COMMAND = 2;
    private static final int START_CONSUMER_COMMAND = 2;
    private static final int START_BUNDLE_COMMAND_COMMAND = 3; //client bundle 45
    private static final int START_EDIT_BUNDLE_COMMAND_COMMAND = 3; //client bundle 45
    private static final int START_EDIT_ACTION_BUNDLE_COMMAND_COMMAND = 45; //client bundle 45 delete
    private static final int START_PAGINATION_COMMAND = 4; // client bundle pagination 2

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

    public static String parseBundleCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_BUNDLE_COMMAND_COMMAND) {
            return split[START_BUNDLE_COMMAND_COMMAND - 1];
        }
        return null;
    }
    public static String parseEditBundleCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_EDIT_BUNDLE_COMMAND_COMMAND) {
            return split[START_EDIT_BUNDLE_COMMAND_COMMAND - 1];
        }
        return null;
    }
    public static String parseEditActionBundleCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_EDIT_ACTION_BUNDLE_COMMAND_COMMAND) {
            return split[START_EDIT_ACTION_BUNDLE_COMMAND_COMMAND - 1];
        }
        return null;
    }
    public static String parsePaginationCommand(String callbackData) {
        String[] split = callbackData.split(" ");
        if (split.length >= START_PAGINATION_COMMAND) {
            return split[START_PAGINATION_COMMAND - 1];
        }
        return null;
    }
}
