package com.pleasurebot.core.bot.handler.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.MainMenuUtil;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.bot.handler.PaginationMenuService;
import com.pleasurebot.core.model.BundleConfig;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.repository.BundleConfigRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientMessageHandler {
    private static final Pair<String, String> BACK = Pair.of("Назад", "client back");
    private static final Pair<String, String> BUNDLE = Pair.of("Сообщения", "client bundle page 0");
    private static final Pair<String, String> BUNDLE_CONFIG = Pair.of("Настройки бота", "client Bundle");
    private static final List<Pair<String, String>> CLIENT_MENU = List.of(BUNDLE, BUNDLE_CONFIG, BACK);

    private static final String CLIENT_MENU_MESSAGE = "Your client options";
    private final TelegramBotApi telegramBotApi;
    private final BundleConfigRepository bundleConfigRepository;
    private final PaginationMenuService paginationMenuService;

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
            case "bundle" -> getBundleCommand(update);
            case "bundleConfig" -> getBundleConfigCommand(chatId, messageId, 1);
            case null -> getClientMenuMessages(chatId, messageId);
            default -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
        };
        if (editMenuMessage == null) {
            return true;
        }
        telegramBotApi.sendEditMenuMessage(editMenuMessage);
        return true;
    }

    private EditMenuMessage getBundleCommand(Update update) {
        paginationMenuService.handleUpdate(update);
        return null;
    }

    public static EditMenuMessage getClientMenuMessages(Long chatId, Long messageId) {
        return EditMenuMessage.builder()
                .chatId(chatId)
                .editedMessageId(messageId)
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
                .editedMessageId(messageId)
                .message(bundleConfigMessage)
                .chatId(chatId)
                .menuList(List.of(Pair.of("Back", "client")))
                .build();
    }
}
