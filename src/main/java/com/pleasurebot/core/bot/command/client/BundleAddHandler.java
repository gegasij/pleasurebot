package com.pleasurebot.core.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.bot.command.StartHandler;
import com.pleasurebot.core.model.BotState;
import com.pleasurebot.core.model.message.SimpleMenuMessage;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BundleAddHandler implements CommandHandler {
    private static final String ADD_BUNDLE = "addBundle";
    private static final List<Pair<String, String>> MENU_LIST = List.of(Pair.of("назад", StartHandler.getMenu()));
    private final String addMessage = "<strong>Отправьте текстовое сообщение боту, чтобы добавить его</strong>\n" + "\n" +
            "   • вы можете прикрепить к сообщению такие медиафайлы, как <u>фотографии</u>, <u>аудиозаписи</u> или даже <u>документы.</u>" + "\n" +
            "   • отправляйте медиафайлы вместе с сообщением если не хотите их разделдить." + "\n" +
            "Теперь каждое ваше сообщение боту будет воспринято как добавление в ваш набор, чтобы отменить это, введите команду /" + StartHandler.getMenu() + " или нажмите кнопку \"назад\".";
    private final UserRepository userRepository;
    private final ClientMenuHandler clientMenuHandler;

    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        String text = update.callbackQuery().data();
        Long id = BotUtil.getChatId(update);
        if (CallbackDataParser.getParameter1(text) == null) {
            userRepository.findByTelegramId(id)
                    .stream()
                    .peek(it -> it.setBotState(BotState.ADD_BUNDLE.getValue()))
                    .findFirst()
                    .ifPresent(userRepository::save);
            return getWaitingBundleMessage();
        }
        return null;
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return ADD_BUNDLE;
    }

    public static String getMenu() {
        return ADD_BUNDLE;
    }

    public SimpleMenuMessage getWaitingBundleMessage() {
        return SimpleMenuMessage.builder()
                .message(addMessage)
                .menuList(MENU_LIST)
                .build();
    }
}
