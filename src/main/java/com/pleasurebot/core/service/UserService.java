package com.pleasurebot.core.service;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.model.BotState;
import com.pleasurebot.core.model.Role;
import com.pleasurebot.core.model.User;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final short GENERATING_NAME_LENGTH = 7;

    public User createUser(String login) {
        login = convertToClearUsername(login);
        User user = new User();
        user.setLogin(login);
        user.setBotState(BotState.NONE.getValue());
        user.setRole(Role.CLIENT.getRoleId());
        user.setPassword(generatePassword());
        userRepository.save(user);
        return user;
    }

    public static String generatePassword() {
        return RandomStringUtils.random(GENERATING_NAME_LENGTH, true, true);
    }

    public Optional<User> getUser(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public void logout(Update update) {
        userRepository.findByTelegramId(BotUtil.getChatId(update))
                .stream()
                .peek(it -> it.setTelegramId(null))
                .findFirst()
                .ifPresent(userRepository::save);
    }

    public User login(Update update) {
        if (update.message().text() != null) {
            String telegramUsername = BotUtil.getTelegramUsername(update);
            String password = CallbackDataParser.getCommand(update.message().text());

            return userRepository.findByLoginAndPassword(telegramUsername, password)
                    .stream()
                    .peek(it -> it.setTelegramId(BotUtil.getChatId(update)))
                    .findFirst()
                    .map(userRepository::save).orElse(null);
        }
        return null;
    }

    public Boolean isThisRole(Update update, Role role) {
        return userRepository.findByTelegramId(BotUtil.getChatId(update))
                .map(it -> it.getRole().equals(role.getRoleId()))
                .orElse(false);
    }

    public String convertToClearUsername(String username) {
        if (username.startsWith("@")) {
            return username.substring(1);
        }
        if (username.startsWith("http")) {
            String[] split = username.split("/");
            if (split.length - 1 >= 0) {
                return split[split.length - 1];
            }
        }
        return username;
    }

}
