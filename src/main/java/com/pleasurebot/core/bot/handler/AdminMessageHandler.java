package com.pleasurebot.core.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.MainMenuUtil;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMessageHandler {
    private static final Pair<String, String> BACK = Pair.of("back", "admin back");
    private static final Pair<String, String> USER_LIST = Pair.of("user list", "admin userlist");
    private static final List<Pair<String, String>> ADMIN_MENU = List.of(BACK, USER_LIST);
    private static final String ADMIN_MENU_MESSAGE = "Your admin options";

    private final TelegramBotApi telegramBotApi;

    public Boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startAdminCommand = CallbackDataParser.parseStartAdminCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();

        EditMenuMessage editMenuMessage = switch (startAdminCommand) {
            case "back" -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
            case "userlist" -> getUserList(chatId, messageId);
            case null -> getAdminMenuMessages(chatId, messageId);
            default -> MainMenuUtil.getMainEditMenuMessage(chatId, messageId);
        };
        telegramBotApi.sendEditMenuMessage(editMenuMessage);
        return true;
    }

    private EditMenuMessage getUserList(Long chatId, Long messageId) {
        return EditMenuMessage.builder()
                .chatId(chatId)
                .editedMessageId(messageId)
                .message("Вася, пупкин")
                .menuList(List.of(Pair.of("Back", "admin"))).build();
    }

    private EditMenuMessage getAdminMenuMessages(Long chatId, Long messageId) {
        return EditMenuMessage.builder()
                .chatId(chatId)
                .editedMessageId(messageId)
                .message(ADMIN_MENU_MESSAGE)
                .menuList(ADMIN_MENU).build();
    }
}
