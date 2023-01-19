package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.handler.AdminMessageHandler;
import com.pleasurebot.core.bot.handler.AnonymousMessageHandler;
import com.pleasurebot.core.bot.handler.ClientMessageHandler;
import com.pleasurebot.core.bot.handler.ConsumerMessageHandler;
import com.pleasurebot.core.model.SendMenuMessage;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import com.pleasurebot.core.service.DriveBundleAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramUpdateHandler {


    private final DriveBundleAdapter driveBundleAdapter;
    private final TelegramBotApi telegramBotApi;
    private final AdminMessageHandler adminMessageHandler;
    private final AnonymousMessageHandler anonymousMessageHandler;
    private final ClientMessageHandler clientMessageHandler;
    private final ConsumerMessageHandler consumerMessageHandler;

    public boolean handleUpdate(Update update) {
        if (update.callbackQuery() != null) {
            String roleCommand = CallbackDataParser.parseRoleCommand(update.callbackQuery().data());
            if (roleCommand != null) {
                if (roleCommand.equals("admin")) {
                    return adminMessageHandler.handleUpdate(update);
                }
                if (roleCommand.equals("anonymous")) {
                    return anonymousMessageHandler.handleUpdate(update);
                }
                if (roleCommand.equals("client")) {
                    return clientMessageHandler.handleUpdate(update);
                }
                if (roleCommand.equals("consumer")) {
                    return consumerMessageHandler.handleUpdate(update);
                }
            }
        } else {
            if (update.callbackQuery() == null && update.message() != null) {
                Long chatId = update.message().chat().id();
                SendMenuMessage mainSendMenuMessage = MainMenuUtil.getMainSendMenuMessage(chatId);
                telegramBotApi.executeSendMenuMessage(mainSendMenuMessage);
                return true;
            }

        }
        return false;
    }


}
