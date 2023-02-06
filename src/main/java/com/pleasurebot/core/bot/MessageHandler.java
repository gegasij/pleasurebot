package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.StartHandler;
import com.pleasurebot.core.bot.command.client.ChangeConsumerCredsHandler;
import com.pleasurebot.core.bot.command.client.NewBundleHandler;
import com.pleasurebot.core.model.BotState;
import com.pleasurebot.core.model.User;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageHandler implements AbstractHandler {
    private final UserService userService;
    private final NewBundleHandler newBundleHandler;
    private final StartHandler startHandler;
    private final ChangeConsumerCredsHandler changeConsumerCredsHandler;

    @Override
    public TelegramMenuMessage handleUpdate(Update update) {
        if (Objects.equals(BotUtil.getCommand(update), StartHandler.getMenu())) {
            return startHandler.handleCommand(update);
        }
        Optional<User> user = userService.getUser(BotUtil.getChatId(update));
        if (user.isEmpty()) {
            return startHandler.handleCommand(update);
        } else if (user.get().getBotState().equals(BotState.ADD_BUNDLE.getValue())) {
            return newBundleHandler.handleCommand(update);
        } else if (user.get().getBotState().equals(BotState.CHANGE_CONSUMER_LOGIN.getValue())) {
            return changeConsumerCredsHandler.handleCommand(update);
        }
        return startHandler.handleCommand(update);
    }
}
