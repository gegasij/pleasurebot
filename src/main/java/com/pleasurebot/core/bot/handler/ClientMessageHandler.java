package com.pleasurebot.core.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.MainMenuUtil;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.model.BundleConfig;
import com.pleasurebot.core.model.EditMenuMessage;
import com.pleasurebot.core.repository.BundleConfigRepository;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientMessageHandler {
    private static final Pair<String, String> BACK = Pair.of("Назад", "client back");
    private static final Pair<String, String> BUNDLE = Pair.of("Сообщения", "client bundle");
    private static final Pair<String, String> BUNDLE_CONFIG = Pair.of("Настройки бота", "client bundleConfig");
    private static final List<Pair<String, String>> CLIENT_MENU = List.of(BUNDLE, BUNDLE_CONFIG, BACK);
    private static final String CLIENT_MENU_MESSAGE = "Your client options";
    private final TelegramBotApi telegramBotApi;
    private final UserRepository userRepository;
    private final BundleConfigRepository bundleConfigRepository;

    public Boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startAdminCommand = CallbackDataParser.parseStartClientCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();

        EditMenuMessage editMenuMessage = switch (startAdminCommand) {
            case "back" -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
            case "bundle" -> getClientMenuMessages(chatId, messageId);
            case "bundleConfig" -> getBundleConfigCommand(chatId, messageId, 1);
            case null -> getClientMenuMessages(chatId, messageId);
            default -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
        };
        telegramBotApi.executeEditMenuMessage(editMenuMessage);
        return true;
    }

    private EditMenuMessage getBundleCommand(Long chatId, Long messageId) {
        return null;
    }

    private EditMenuMessage getClientMenuMessages(Long chatId, Long messageId) {
        return EditMenuMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .message(CLIENT_MENU_MESSAGE)
                .menuList(CLIENT_MENU).build();
    }

    private EditMenuMessage getBundleConfigCommand(Long chatId, Long messageId, Integer userId) {
        List<BundleConfig> bundleByUserId = bundleConfigRepository.getBundleByOwnerId(userId);

        String bundleConfigMessage = bundleByUserId.stream()
                .findFirst()
                .map(BundleConfig::toString)
                .orElse("Bundle not found");

        return EditMenuMessage.builder()
                .messageId(messageId)
                .message(bundleConfigMessage)
                .chatId(chatId)
                .menuList(List.of(Pair.of("Back", "client")))
                .build();
    }
}
