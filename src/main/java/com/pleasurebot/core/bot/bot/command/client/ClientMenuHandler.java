package com.pleasurebot.core.bot.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.AnonymousMessageHandler;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.model.Role;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.model.message.TelegramMenuMessage;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import com.pleasurebot.core.bot.model.message.CustomTelegramMenuMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ClientMenuHandler implements CommandHandler {
    private static final String CLIENT_MENU_COMMAND = "clientMenu";
    private static final Pair<String, String> CHANGE_CUSTOMER_CREDENTIALS = Pair.of("настройка", ChangeConsumerCredsHandler.getMenu());
    private static final Pair<String, String> LOGOUT = Pair.of("выйти", ClientMenuHandler.getMenuCommand() + " " + "logout");
    private static final Pair<String, String> MESSAGES = Pair.of("сообщения", MessagesMenuHandler.getMenu());
    private static final List<Pair<String, String>> ROW1_MENU = List.of(MESSAGES, CHANGE_CUSTOMER_CREDENTIALS);
    private static final List<Pair<String, String>> ROW2_MENU = List.of(LOGOUT);
    private static final List<List<Pair<String, String>>> CLIENT_MENU = List.of(ROW1_MENU,ROW2_MENU);


    private static final String CLIENT_MENU_MESSAGE = "Your client options";
    private final UserService userService;
    private final AnonymousMessageHandler anonymousMessageHandler;

    public TelegramMenuMessage handleCommand(Update update) {

        if (checkAccessToCommand(update)) {
            if (update.callbackQuery()!=null){
                if (Objects.equals(CallbackDataParser.getParameter1(update.callbackQuery().data()), "logout")) {
                    return logout(update);
                }
            }
            return getClientMenuMessages();
        }
        return null;
    }

    private SimpleMenuMessage logout(Update update) {
        userService.logout(update);
        return anonymousMessageHandler.handleCommand(update);
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return userService.isThisRole(update, Role.CLIENT);
    }

    @Override
    public String getCommand() {
        return CLIENT_MENU_COMMAND;
    }

    public static String getMenuCommand() {
        return CLIENT_MENU_COMMAND;
    }

    public static CustomTelegramMenuMessage getClientMenuMessages() {
        return CustomTelegramMenuMessage.builder()
                .message(CLIENT_MENU_MESSAGE)
                .inlineKeyboardButtons(CLIENT_MENU)
                .build();
    }
}
