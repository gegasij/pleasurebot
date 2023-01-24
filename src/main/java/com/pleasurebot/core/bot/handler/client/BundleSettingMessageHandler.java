package com.pleasurebot.core.bot.handler.client;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pleasurebot.core.bot.MainMenuUtil;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.message.EditMenuMessage;
import com.pleasurebot.core.repository.BundlePagingRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BundleSettingMessageHandler {
    private final Pair<String, String> EDIT = Pair.of("изменить", "edit");
    private final Pair<String, String> DELETE = Pair.of("удалить", "delete");
    private final Pair<String, String> BACK = Pair.of("Назад", "client bundle");
    private final List<Pair<String, String>> ACTION_MENU = List.of(BACK, EDIT, DELETE);
    private final BundlePagingRepository bundlePagingRepository;
    private final TelegramBotApi telegramBotApi;

    public boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startBundleCommand = CallbackDataParser.parseEditBundleCommand(data);
        Optional<Bundle> byId = Optional.empty();
        if (NumberUtils.isCreatable(startBundleCommand)) {
            Integer it = NumberUtils.toInt(startBundleCommand, -1);
            byId = bundlePagingRepository.findById(it);
        }
        if (byId.isPresent()) {
            Bundle bundle = byId.get();
            String s = CallbackDataParser.parseEditActionBundleCommand(data);
            Long messageId = update.callbackQuery().message().messageId().longValue();
            Long chatId = update.callbackQuery().message().chat().id();
            if (s == null) {
                sendEditActionMenu(bundle, messageId, chatId);
            } else {
                EditMenuMessage editMenuMessage = switch (startBundleCommand) {
                    case "delete" -> deleteMessage(bundle, chatId, messageId);
                    case "edit" -> editMessage(bundle, chatId, messageId);
                    default -> ClientMessageHandler.getClientMenuMessages(chatId, messageId);
                };
            }
        } else return false;
        return true;
    }

    private EditMenuMessage editMessage(Bundle bundle, Long chatId, Long messageId) {
        return null;
    }

    private EditMenuMessage deleteMessage(Bundle bundle, Long chatId, Long messageId) {
        EditMenuMessage build = EditMenuMessage.builder()
                .message("Сообщение удалено")
                .menuList(List.of(Pair.of("Готвоо!", BACK.getSecond())))
                .editedMessageId(messageId)
                .chatId(chatId)
                .build();

        bundlePagingRepository.delete(bundle);
        return build;
    }

    private EditMenuMessage sendEditActionMenu(Bundle bundle, Long messageId, Long chatId) {
        EditMenuMessage build = EditMenuMessage.builder()
                .message("Сообщение: " + bundle.getMessage())
                .menuList(ACTION_MENU)
                .editedMessageId(messageId)
                .chatId(chatId)
                .build();

        return build;
    }
}
