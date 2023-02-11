package com.pleasurebot.core.bot.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnonymousMessageHandler implements CommandHandler {
    private static final String ANONYMOUS_MENU_COMMAND = "anonymousMenu";
    private static final String ANON_START_MESSAGE = "Привет! На связи ваш персональный lovelyBot" + "\n" + " введите пароль, чтобы продолжить";
    private static final String SUCCESS_LOGIN_MESSAGE = "<strong>Отлично! </strong> А теперь приступим";
    private static final List<Pair<String, String>> SUCCESS_LOGIN_MENU = List.of(Pair.of("Поехали!", StartHandler.getMenu()));
    private static final String FAILED_LOGIN_MESSAGE = "Что-то пошло не так... Попробуй еще раз или попроси партнера скинуть пароль заново";
    private final UserService userService;

    public SimpleMenuMessage handleCommand(Update update) {
        if (update.message() == null) {
            return SimpleMenuMessage.builder()
                    .message(ANON_START_MESSAGE)
                    .build();
        }
        if (update.message() != null) {
            User login = userService.login(update);
            if (login != null) {
                return successLoginMessage(login.getLogin());
            } else return failedLoginMessage();
        }
        return null;
    }

    private SimpleMenuMessage failedLoginMessage() {
        return SimpleMenuMessage.builder()
                .message(FAILED_LOGIN_MESSAGE)
                .build();
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return ANONYMOUS_MENU_COMMAND;
    }

    private static SimpleMenuMessage successLoginMessage(String username) {
        return SimpleMenuMessage.builder()
                .message(SUCCESS_LOGIN_MESSAGE)
                .menuList(SUCCESS_LOGIN_MENU)
                .build();
    }
}
