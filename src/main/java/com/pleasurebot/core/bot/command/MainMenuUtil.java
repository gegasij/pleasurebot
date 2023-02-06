package com.pleasurebot.core.bot.command;


import com.pleasurebot.core.model.message.SimpleMenuMessage;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

import java.util.List;

@UtilityClass
public class MainMenuUtil {
    private static final Pair<String, String> ADMIN_MENU_BUTTON = Pair.of("admin", "admin");
    private static final Pair<String, String> ANONYMOUS_MENU_BUTTON = Pair.of("anonymous", "anonymous");
    private static final Pair<String, String> CLIENT_LIST = Pair.of("client", "client");
    private static final Pair<String, String> CONSUMER_LIST = Pair.of("consumer", "consumer");
    private static final String MAIN_MENU_MESSAGE = "hello From PleasureBot, where we going";
    private static final List<Pair<String, String>> MAIN_MENU_BUTTON_LIST = List.of(ADMIN_MENU_BUTTON,
            ANONYMOUS_MENU_BUTTON,
            CLIENT_LIST,
            CONSUMER_LIST, Pair.of("test", "test"));

    public SimpleMenuMessage getMainEditMenuMessage(@NonNull Long chatId) {
        return SimpleMenuMessage.builder()
                .menuList(MAIN_MENU_BUTTON_LIST)
                .message(MAIN_MENU_MESSAGE).build();
    }

}
