package com.pleasurebot.core.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.admin.AdminMenuHandler;
import com.pleasurebot.core.bot.command.client.ClientMenuHandler;
import com.pleasurebot.core.bot.command.consumer.ConsumerMessageHandler;
import com.pleasurebot.core.model.BotState;
import com.pleasurebot.core.model.Role;
import com.pleasurebot.core.model.User;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartHandler implements CommandHandler {
    private static final String MAIN_MENU_COMMAND = "start";
    private static final Pair<String, String> adminMenuButtons = Pair.of("admin", AdminMenuHandler.getMenuCommand());
    private static final Pair<String, String> clientMenuButton = Pair.of("Парень", ClientMenuHandler.getMenuCommand());
    private static final Pair<String, String> consumerMenuButton = Pair.of("Девушка", ConsumerMessageHandler.getMenuCommand());
    private static final List<Pair<String, String>> MENU_BUTTONS = List.of(
            clientMenuButton,
            consumerMenuButton
    );
    private final UserService userService;
    private final AnonymousMessageHandler anonymousMessageHandler;
    private final ClientMenuHandler clientMenuHandler;
    private final ConsumerMessageHandler consumerMessageHandler;
    private final UserRepository userRepository;

    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        Long chatId = BotUtil.getChatId(update);
        Optional<User> user = userService.getUser(chatId);
        if (user.isEmpty()) {
            return anonymousMessageHandler.handleCommand(update);
        } else if (user.get().getRole().equals(Role.CLIENT.getRoleId())) {
            user.filter(it->it.getBotState().equals(BotState.ADD_BUNDLE.getValue()))
                    .stream()
                    .peek(it->it.setBotState(BotState.NONE.getValue()))
                    .findFirst()
                    .ifPresent(userRepository::save);
            return clientMenuHandler.handleCommand(update);
        } else if (user.get().getRole().equals(Role.CONSUMER.getRoleId())) {
            return consumerMessageHandler.handleCommand(update);
        } else throw new RuntimeException("Could not redirect to right handler "+ update);
    }


    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return MAIN_MENU_COMMAND;
    }

    public static String getMenu() {
        return MAIN_MENU_COMMAND;
    }
}
