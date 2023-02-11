package com.pleasurebot.core.bot.bot.command.consumer;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.AnonymousMessageHandler;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ConsumerMessageHandler implements CommandHandler {

    private static final String CONSUMER_COMMAND = "consumer";
    private static final Pair<String, String> BACK = Pair.of("Выйти", "%s logout".formatted(CONSUMER_COMMAND));
    private static final Pair<String, String> MESSAGE = Pair.of("Получить сообщение", RequestMessageHandler.getCommandMenu());
    private static final List<Pair<String, String>> CLIENT_MENU = List.of(MESSAGE, BACK);
    private static final String CLIENT_MENU_MESSAGE = "Нажмите получить сообщение, чтобы посмотреть, что для вас приготовлено";
    private final UserService userService;
    private final AnonymousMessageHandler anonymousMessageHandler;

    public SimpleMenuMessage handleCommand(Update update) {
        if (checkAccessToCommand(update)) {
            if (update.callbackQuery() != null) {
                if (Objects.equals(CallbackDataParser.getParameter1(update.callbackQuery().data()), "logout")) {
                    return logout(update);
                }
            }
            return getConsumerMenuMessages();
        }
        return null;
    }

    private SimpleMenuMessage logout(Update update) {
        userService.logout(update);
        return anonymousMessageHandler.handleCommand(update);
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return CONSUMER_COMMAND;
    }

    public static String getMenuCommand() {
        return CONSUMER_COMMAND;
    }

    private SimpleMenuMessage getConsumerMenuMessages() {
        return SimpleMenuMessage.builder()
                .message(CLIENT_MENU_MESSAGE)
                .menuList(CLIENT_MENU)
                .build();
    }
}
