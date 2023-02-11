package com.pleasurebot.core.bot.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import com.pleasurebot.core.bot.service.BundleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DeleteBundleHandler implements CommandHandler {
    private static final String DELETE_BUNDLE_COMMAND = "deleteBundle";
    private final BundleService bundleService;

    private static final Pair<String, String> DELETE_BUNDLE_RESULT_MESSAGE = Pair.of("Готово!", ClientMenuHandler.getMenuCommand());

    @Override
    public SimpleMenuMessage handleCommand(Update update) {
        String data = update.callbackQuery().data();

        String startBundleCommand = CallbackDataParser.getCommand(data);
        String parameter1 = CallbackDataParser.getParameter1(data);
        if (Objects.equals(startBundleCommand, DELETE_BUNDLE_COMMAND)) {
            if (NumberUtils.isCreatable(parameter1)) {
                int bundleId = NumberUtils.toInt(parameter1);
                return deleteBundle(bundleId);
            }
        }
        return null;
    }

    private SimpleMenuMessage deleteBundle(Integer bundleId) {
        bundleService.deleteBundle(bundleId);

        return SimpleMenuMessage.builder()
                .message("Сообщение удалено")
                .menuList(List.of(DELETE_BUNDLE_RESULT_MESSAGE))
                .build();
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return DELETE_BUNDLE_COMMAND;
    }

    public static String getMenuCommand() {
        return DELETE_BUNDLE_COMMAND;
    }

}
