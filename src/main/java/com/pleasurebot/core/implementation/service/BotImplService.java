package com.pleasurebot.core.implementation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.implementation.model.*;
import com.pleasurebot.core.implementation.util.BotUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BotImplService {
    private final CommandResolver commandResolver;
    private final UserService userService;

    public BaseRequest<?, ?> resolveMessage(BotImpl from, Update update) {
        String command = BotUtil.getCommand(update);
        if (command != null) {
            Object o = commandResolver.resolveCommand(command);
            if (o instanceof AnswerMessage answerMessage) {

                SendMessage sendMessage = TelegramBotAdapter.adaptMessage(answerMessage, BotUtil.getChatId(update));

                List<InlineKeyboardButton> inlineKeyboardButtons = TelegramBotAdapter.getInlineKeyboardButtons(answerMessage);
                List<KeyboardButton> publicKeyboardButtons = TelegramBotAdapter.getPublicKeyboardButtons(from.getAnswerMessage());
                if (!inlineKeyboardButtons.isEmpty()) {
                    sendMessage.replyMarkup(new InlineKeyboardMarkup(inlineKeyboardButtons.toArray(new InlineKeyboardButton[0])));
                } else if (!publicKeyboardButtons.isEmpty()) {
                    sendMessage.replyMarkup(new ReplyKeyboardMarkup(publicKeyboardButtons.toArray(new KeyboardButton[0])));
                }
                return sendMessage;
            }
            if (o instanceof Invoice invoice) {
                return TelegramBotAdapter.adaptInvoice(invoice, BotUtil.getChatId(update), from.getPayment().getProviderToken());
            }
        }
        return null;
    }

    public AnswerPreCheckoutQuery resolvePreCheckoutQuery(Update update) {
        return new AnswerPreCheckoutQuery(update.preCheckoutQuery().id());
    }

    public BaseRequest<?, ?> resolveSuccessPayment(Update update) {
        User user = userService.createUser(BotUtil.getTelegramUsername(update));
        String text = "Спасибо за приобритение MylovelyBot! \nTеперь переходите к боту @myLoveely_bot и введите ему этот пароль <code>" + user.getPassword() + "</code>";
        String link="https://drive.google.com/file/d/1hJKioRBh1F22KdYvKRn2N0rYJw908BJF/view?usp=sharing";
        text = text + "\nЕсли возникнут вопросы использования бота, можете смело обращаться ко мне в личку @ireut, или вы можете посмотреть обучающий ролик по ссылке "+link ;
        return new SendMessage(BotUtil.getChatId(update), text)
                .parseMode(ParseMode.HTML);
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BotImpl bot = new BotImpl();
        bot.setBotToken("dsadas");
        Payment payment = new Payment();
        payment.setProviderToken("dasda");
        Invoice invoice = new Invoice();
        invoice.setInvoiceCommand("dasd");
        invoice.setDescription("dasd");
        invoice.setTitle("dasd");
        invoice.setPayload("dasd");
        invoice.setCurrency("USD");
        Price pr = new Price();
        pr.setText("text");
        pr.setAmount(3223);
        invoice.setPrice(List.of(pr));
        payment.setInvoice(List.of(invoice));
        bot.setPayment(payment);
        AnswerMessage answerMessage = new AnswerMessage();
        answerMessage.setCommand("dsa");
        answerMessage.setText("dsa");
        answerMessage.setPublic(true);
        MenuButton menuButton = new MenuButton();
        menuButton.setText("dasda");
        menuButton.setLink("dasda");
        menuButton.setCommand("dasda");
        answerMessage.setMenuButtons(List.of(menuButton));
        bot.setAnswerMessage(List.of(answerMessage));
        objectMapper.writeValue(new File("src/main/resources/bot.json"), bot);
    }
}

