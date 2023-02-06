package com.pleasurebot.core.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.model.Role;
import com.pleasurebot.core.model.message.CustomTelegramMenuMessage;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessagesMenuHandler implements CommandHandler {
    private static final String MESSAGES_MENU = "messageMenu";
    private static final Pair<String, String> BUNDLE = Pair.of("просмотреть", BundlePageHandler.getCommandMenu(0));
    private static final Pair<String, String> ADD_BUNDLE = Pair.of("добавить", BundleAddHandler.getMenu());
    private static final Pair<String, String> BACK = Pair.of("назад", ClientMenuHandler.getMenuCommand());
    private static final Pair<String, String> SETTINGS = Pair.of("настроить", BundleConfigEditMenuHandler.getMenu());
    private static final List<Pair<String, String>> ROW1_MENU = List.of(BUNDLE, ADD_BUNDLE);
    private static final List<Pair<String, String>> ROW2_MENU = List.of(BACK, SETTINGS);
    private static final List<List<Pair<String, String>>> MENU = List.of(ROW1_MENU,ROW2_MENU);
    private final UserService userService;


    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        if(checkAccessToCommand(update)){
            return CustomTelegramMenuMessage.builder()
                    .message("Выберете, что хотите сделать")
                    .inlineKeyboardButtons(MENU)
                    .build();
        }
        return null;
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return userService.isThisRole(update, Role.CLIENT);
    }

    @Override
    public String getCommand() {
        return MESSAGES_MENU;
    }

    public static String getMenu() {
        return MESSAGES_MENU;
    }
}
