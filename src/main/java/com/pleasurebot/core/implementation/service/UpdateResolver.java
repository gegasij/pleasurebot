package com.pleasurebot.core.implementation.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pleasurebot.core.implementation.model.BotImpl;
import com.pleasurebot.core.implementation.model.Client;
import com.pleasurebot.core.implementation.util.BotUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("implementationResolver")
public class UpdateResolver {
    private final TelegramBot telegramBot;
    private final BotImpl botImpl;
    private final BotImplService botImplService;
    private final ClientService clientService;

    public UpdateResolver(@Qualifier("implementation") TelegramBot telegramBot, BotImpl botImpl, BotImplService botImplService, ClientService clientService) {
        this.telegramBot = telegramBot;
        this.botImpl = botImpl;
        this.botImplService = botImplService;
        this.clientService = clientService;
    }

    public void resolveUpdate(Update update) {
        Client client = clientService.getOrCreateClient(BotUtil.getChatId(update));
        BaseRequest<?, ?> request = null;
        if (BotUtil.getCommand(update) != null) {
            request = botImplService.resolveMessage(botImpl, update);
        } else if (update.preCheckoutQuery() != null) {
            request = botImplService.resolvePreCheckoutQuery(update);
        } else if (update.message().successfulPayment() != null) {
            client.setIsPayed(true);
            request = botImplService.resolveSuccessPayment(update);
        }
        if (request != null) {
            telegramBot.execute(request);
        }
    }
}
