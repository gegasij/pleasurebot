package com.pleasurebot.core.bot.command.admin;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.bot.command.MainMenuUtil;
import com.pleasurebot.core.model.message.SimpleMenuMessage;
import com.pleasurebot.core.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMenuHandler implements CommandHandler {
    private static final String ADMIN_MENU_COMMAND = "adminMenu";
    private static final Pair<String, String> BACK = Pair.of("back", "admin back");
    private static final Pair<String, String> USER_LIST = Pair.of("user list", "admin userlist");
    private static final List<Pair<String, String>> ADMIN_MENU = List.of(BACK, USER_LIST);
    private static final String ADMIN_MENU_MESSAGE = "Your admin options";

    @Override
    public SimpleMenuMessage handleCommand(Update update) {
        String data = update.callbackQuery().data();
        String startAdminCommand = CallbackDataParser.getCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();

        return switch (startAdminCommand) {
            case "back" -> MainMenuUtil.getMainEditMenuMessage(chatId);
            case "userlist" -> getUserList(chatId, messageId);
            case null -> getAdminMenuMessages(chatId, messageId);
            default -> MainMenuUtil.getMainEditMenuMessage(chatId);
        };
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return null;
    }

    @Override
    public String getCommand() {
        return ADMIN_MENU_COMMAND;
    }

    public static String getMenuCommand(){
        return ADMIN_MENU_COMMAND;
    }

    private SimpleMenuMessage getUserList(Long chatId, Long messageId) {
        return SimpleMenuMessage.builder()
                .message("Вася, пупкин")
                .menuList(List.of(Pair.of("Back", "admin"))).build();
    }

    private SimpleMenuMessage getAdminMenuMessages(Long chatId, Long messageId) {
        return SimpleMenuMessage.builder()
                .message(ADMIN_MENU_MESSAGE)
                .menuList(ADMIN_MENU).build();
    }
}
