package com.pleasurebot.core.payment.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.SendInvoice;
import com.pengrad.telegrambot.request.SendMessage;
import com.pleasurebot.core.bot.service.utils.BotUtil;
import com.pleasurebot.core.payment.service.ActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component(value = "activatorBotResolver")
@RequiredArgsConstructor
public class UpdateResolver {
    private final TelegramBotApi telegramBotApi;
    private final ActivationService activationService;

    public void resolveUpdate(Update update) {
        if (update.preCheckoutQuery() != null) {
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery(update.preCheckoutQuery().id());
            telegramBotApi.sendMessage(answerPreCheckoutQuery);
            return;
        }
        LabeledPrice labeledPrice = new LabeledPrice("botPrice1", 150);
        LabeledPrice labeledPrice2 = new LabeledPrice("botPrice2", 170);
        SendInvoice sendInvoice = new SendInvoice(BotUtil.getChatId(update),
                "title payment",
                "descr payment",
                "ppayload",
                "284685063:TEST:ZWFmZmY0Zjk2YjY0",
                "USD", labeledPrice, labeledPrice2);
        sendInvoice.maxTipAmount(100);
        sendInvoice.suggestedTipAmounts(List.of(1, 2, 3).toArray(Integer[]::new));
        telegramBotApi.sendMessage(sendInvoice);

        if (update.message() != null) {
            if (update.message().text() != null) {
                String telegramUsername = BotUtil.getTelegramUsername(update);
                if (update.message().text().contains("@")) {
                    String result = activationService.activateUser(telegramUsername, update.message().text());
                    SendMessage sendMessage = new SendMessage(BotUtil.getChatId(update), result);
                    telegramBotApi.sendMessage(sendMessage);
                } else {
                    String message = "С помощью бота вы подготавливаете набор сообщений которые будут отправлены вашей второй половинке, настраиваете как и с каким промежутком бот будет их присылать <strong>И ВСЕ!</strong> Дарите партнеру! \n" +
                            "Для активации бота перейдите на страницу ниже.";
                    SendMessage sendMessage = new SendMessage(BotUtil.getChatId(update), message);
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("MylovelyBot.com");
                    inlineKeyboardButton.url("http://mylovelybot.com/");
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButton);
                    sendMessage.parseMode(ParseMode.HTML);
                    sendMessage.replyMarkup(inlineKeyboardMarkup);
                    telegramBotApi.sendMessage(sendMessage);
                }
            }
        } else {
            SendMessage sendMessage = new SendMessage(BotUtil.getChatId(update), "Доброго дня! \n введите мне свою почту, которой вы произвели оплату, я создам для вас аккаунт и пришлю пароль");
            sendMessage.parseMode(ParseMode.HTML);
            telegramBotApi.sendMessage(sendMessage);
        }
    }
}
