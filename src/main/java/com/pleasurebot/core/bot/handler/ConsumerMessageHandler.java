package com.pleasurebot.core.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.MainMenuUtil;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.model.ConsumerBundleResponse;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.repository.BundleRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import com.pleasurebot.core.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsumerMessageHandler {
    private static final Pair<String, String> BACK = Pair.of("Назад", "consumer back");
    private static final Pair<String, String> MESSAGE = Pair.of("Получить сообщение", "consumer requestMessage");
    private static final List<Pair<String, String>> CLIENT_MENU = List.of(MESSAGE, BACK);
    private static final String CLIENT_MENU_MESSAGE = "Your consumer options";
    private final TelegramBotApi telegramBotApi;
    private final ConsumerService consumerService;
    private final BundleRepository bundleRepository;

    public Boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startAdminCommand = CallbackDataParser.parseStartConsumerCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();

        EditMenuMessage editMenuMessage = switch (startAdminCommand) {
            case "back" -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
            case "requestMessage" -> getRequestMessages(chatId, messageId);
            case null -> getConsumerMenuMessages(chatId, messageId);
            default -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
        };
        telegramBotApi.sendEditMenuMessage(editMenuMessage);
        return true;
    }

    private EditMenuMessage getRequestMessages(Long chatId, Long messageId) {
        ConsumerBundleResponse consumerBundleResponse = consumerService.requestBundle(1);
        if (consumerBundleResponse.getBasicBundle() != null) {
            bundleRepository.updateLastRequestTime(consumerBundleResponse.getBasicBundle().getId(), LocalDateTime.now());
            return EditMenuMessage.builder()
                    .chatId(chatId)
                    .editedMessageId(messageId)
                    .message(consumerBundleResponse.getBasicBundle().getOrder() + ". " + consumerBundleResponse.getBasicBundle().getMessage())
                    .menuList(CLIENT_MENU)
                    .build();
        } else {
            return EditMenuMessage.builder()
                    .chatId(chatId)
                    .editedMessageId(messageId)
                    .message(consumerBundleResponse.getMessage())
                    .menuList(CLIENT_MENU)
                    .build();
        }
    }

    private EditMenuMessage getConsumerMenuMessages(Long chatId, Long messageId) {
        return EditMenuMessage.builder()
                .chatId(chatId)
                .editedMessageId(messageId)
                .message(CLIENT_MENU_MESSAGE)
                .menuList(CLIENT_MENU)
                .build();
    }
}
