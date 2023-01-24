package com.pleasurebot.core.bot.handler.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.handler.PaginationMenuService;
import com.pleasurebot.core.repository.BundlePagingRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BundleMessageHandler {
    private final PaginationMenuService paginationMenuService;


    public Boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startBundleCommand = CallbackDataParser.parseBundleCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();
        if (Objects.equals(startBundleCommand, "pagination")) {
            return paginationMenuService.handleUpdate(update);
        }
        if (NumberUtils.isCreatable(startBundleCommand)) {
            int bundleId = NumberUtils.toInt(startBundleCommand);

        }

        return false;
    }
}
