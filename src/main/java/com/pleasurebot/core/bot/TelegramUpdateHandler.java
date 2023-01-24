package com.pleasurebot.core.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.handler.AdminMessageHandler;
import com.pleasurebot.core.bot.handler.AnonymousMessageHandler;
import com.pleasurebot.core.bot.handler.client.ClientMessageHandler;
import com.pleasurebot.core.bot.handler.ConsumerMessageHandler;
import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.model.message.MenuMessage;
import com.pleasurebot.core.repository.BundlePagingRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramUpdateHandler {
    private final TelegramBotApi telegramBotApi;
    private final AdminMessageHandler adminMessageHandler;
    private final AnonymousMessageHandler anonymousMessageHandler;
    private final ClientMessageHandler clientMessageHandler;
    private final ConsumerMessageHandler consumerMessageHandler;
    private final BundlePagingRepository bundlePagingRepository;

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
                if (roleCommand.equals("test")) {
                    Message message = update.callbackQuery().message();
                    //telegramBotApi.sendEditMenuMessage(null);
                }
            }
        } else {
            if (update.callbackQuery() == null && update.message() != null) {
                Long chatId = update.message().chat().id();
                MenuMessage mainMenuMessage = MainMenuUtil.getMainSendMenuMessage(chatId);
                telegramBotApi.sendMenuMessage(mainMenuMessage);
                return true;
            }

        }
        return false;
    }

    private EditMenuMessage getPaging(Long chatId, Integer messageId) {
        PageRequest of = PageRequest.of(0, 2);
        Page<Bundle> all = bundlePagingRepository.findAll(of);
        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();
        List<Pair<String, String>> pairs = new java.util.ArrayList<>(all.getContent().stream()
                .map(Bundle::getMessage)
                .map(it -> Pair.of(it, it))
                .toList());
        String s= "total elements "+totalElements+" total Pages "+totalPages;
        totalElements = all.getTotalElements();
        totalPages = all.getTotalPages();
        String s2= " total elements2 "+totalElements+" total Pages2 "+totalPages;
        pairs.add(Pair.of("back", "client"));

        return EditMenuMessage.builder()
                .menuList(pairs)
                .chatId(chatId)
                .editedMessageId(messageId.longValue())
                .message(s+s2)
                .build();
    }


}
