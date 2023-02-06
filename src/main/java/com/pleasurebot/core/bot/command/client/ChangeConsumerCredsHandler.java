package com.pleasurebot.core.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.model.BasicBundleConfig;
import com.pleasurebot.core.model.BotState;
import com.pleasurebot.core.model.Role;
import com.pleasurebot.core.model.User;
import com.pleasurebot.core.model.message.SimpleMenuMessage;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.BundleConfigService;
import com.pleasurebot.core.service.utils.CallbackDataParser;
import com.pleasurebot.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChangeConsumerCredsHandler implements CommandHandler {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BundleConfigService bundleConfigService;
    private final ClientMenuHandler clientMenuHandler;
    private static final String CHANGE_CONSUMER_CREDS = "changeConsumerCreds";
    private static final Pair<String, String> BACK = Pair.of("Назад", ClientMenuHandler.getMenuCommand());
    private static final Pair<String, String> RELOAD_PASSWORD = Pair.of("Сгенерировать новый пароль", CHANGE_CONSUMER_CREDS + " " + "generatePass");
    private static final List<Pair<String, String>> MENU_LIST = List.of(BACK, RELOAD_PASSWORD);

    @Override
    public TelegramMenuMessage handleCommand(Update update) {

        User user = userService.getUser(BotUtil.getChatId(update))
                .filter(it -> it.getRole().equals(Role.CLIENT.getRoleId()))
                .orElseThrow(() -> new RuntimeException("user has no access for it"));

        BasicBundleConfig bbc = bundleConfigService.getBasicBundleConfig(user.getId());

        if (update.callbackQuery() != null) {
            String parameter1 = CallbackDataParser.getParameter1(update.callbackQuery().data());
            if (Objects.isNull(parameter1)) {
                user.setBotState(BotState.CHANGE_CONSUMER_LOGIN.getValue());
                userRepository.save(user);
                return printCurrentCredsState(bbc);
            } else if (Objects.equals("generatePass", parameter1)) {
                userRepository.findById(bbc.getAttachedUserId())
                        .stream()
                        .peek(it -> it.setPassword(UserService.generatePassword()))
                        .forEach(userRepository::save);
                return printCurrentCredsState(bbc);
            } else if (Objects.equals("back", parameter1)) {
                Optional.of(user)
                        .filter(it -> it.getBotState().equals(BotState.CHANGE_CONSUMER_LOGIN.getValue()))
                        .ifPresent(it -> {
                            it.setBotState(BotState.NONE.getValue());
                            userRepository.save(it);
                        });
                return clientMenuHandler.handleCommand(update);
            }
        }
        if (update.message() != null && user.getBotState().equals(BotState.CHANGE_CONSUMER_LOGIN.getValue())) {
            return resolveNewPartnerUsername(update, bbc);
        }
        return null;
    }


    private SimpleMenuMessage resolveNewPartnerUsername(Update update, BasicBundleConfig bbc) {
        String username = userService.convertToClearUsername(update.message().text());
        userRepository.findById(bbc.getAttachedUserId())
                .stream()
                .peek(it -> it.setLogin(username))
                .forEach(userRepository::save);
        return printCurrentCredsState(bbc);
    }

    @Nullable
    private SimpleMenuMessage printCurrentCredsState(BasicBundleConfig basicBundleConfig) {
        Integer attachedUserId = basicBundleConfig.getAttachedUserId();
        return userRepository.findById(attachedUserId)
                .map(it -> Optional.ofNullable(it.getLogin())
                        .map(it2 -> buildMessage(it2, it.getPassword()))
                        .orElse(buildMessage(it.getPassword())))
                .orElse(null);
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return null;
    }

    @Override
    public String getCommand() {
        return CHANGE_CONSUMER_CREDS;
    }

    public static String getMenu() {
        return CHANGE_CONSUMER_CREDS;
    }

    private SimpleMenuMessage buildMessage(String password) {
        String loginMessage = "<strong> Имя пользователя партнера незадано</strong> \n скопируйте имя пользователя в telegram вашего партера и пришлите его боту\n";
        String passwordMessage = "<strong>    Пароль:" + password + "</strong>";
        return SimpleMenuMessage.builder()
                .message(loginMessage + passwordMessage)
                .menuList(MENU_LIST)
                .build();
    }

    private SimpleMenuMessage buildMessage(String login, String password) {
        String loginMessage = "<strong>    Имя пользователя: @" + login + "</strong> \n";
        String passwordMessage = "<strong>    Пароль:" + password + "</strong>\n";
        String helperMessage = " Вы все равно можете изменить имя пользователя партера если сейчас отправите его мне";
        return SimpleMenuMessage.builder()
                .message(loginMessage + passwordMessage + helperMessage)
                .menuList(MENU_LIST)
                .build();
    }

}
