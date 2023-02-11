package com.pleasurebot.core.payment.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "activatorApi")
public class TelegramBotApi {

    public TelegramBotApi(@Qualifier("activatorBot") TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    private final TelegramBot telegramBot;

    public BaseResponse sendMessage(BaseRequest<?, ?> editMessageText) {
        return telegramBot.execute(editMessageText);
    }


}
