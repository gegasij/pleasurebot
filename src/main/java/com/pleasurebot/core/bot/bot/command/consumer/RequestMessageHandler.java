package com.pleasurebot.core.bot.bot.command.consumer;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.TelegramBotApi;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.repository.BundleConfigRepository;
import com.pleasurebot.core.bot.model.ConsumerBundleResponse;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.model.message.TelegramMenuMessage;
import com.pleasurebot.core.bot.repository.AttachmentRepository;
import com.pleasurebot.core.bot.repository.BundleRepository;
import com.pleasurebot.core.bot.repository.UserRepository;
import com.pleasurebot.core.bot.service.utils.BotUtil;
import com.pleasurebot.core.bot.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestMessageHandler implements CommandHandler {
    private static final String REQUEST_MESSAGE_COMMAND = "requestMessage";
    private final BundleRepository bundleRepository;
    private final BundleConfigRepository bundleConfigRepository;
    private final AttachmentRepository attachmentRepository;
    private final ConsumerService consumerService;
    private final UserRepository userRepository;
    private final TelegramBotApi telegramBotApi;
    private static final Pair<String, String> BACK = Pair.of("назад", ConsumerMessageHandler.getMenuCommand());
    private static final List<Pair<String, String>> successMenu = List.of(Pair.of("Еще сообщение!", RequestMessageHandler.getCommandMenu()), BACK);
    private static final List<Pair<String, String>> failedMenu = List.of(Pair.of("Повторить!", RequestMessageHandler.getCommandMenu()), BACK);

    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        User user = userRepository.findByTelegramId(BotUtil.getChatId(update)).orElseThrow(() -> new RuntimeException("could not find user" + update));
        ConsumerBundleResponse consumerBundleResponse = consumerService.requestBundle(user.getId());
        if (consumerBundleResponse.getBasicBundle() != null) {
            SimpleMenuMessage build = SimpleMenuMessage.builder()
                    .message(consumerBundleResponse.getBasicBundle().getMessage())
                    .menuList(successMenu)
                    .build();
            Optional.of(attachmentRepository.findByBundleId(consumerBundleResponse.getBasicBundle().getId()))
                    .filter(it -> !it.isEmpty())
                    .ifPresent(build::setAttachments);
            bundleRepository.updateLastRequestTime(consumerBundleResponse.getBasicBundle().getId(), LocalDateTime.now());
            bundleConfigRepository.updateLastRequestTime(user.getId());
            telegramBotApi.executeSendMessage(build, BotUtil.getChatId(update));
            return null;
        } else {
            SimpleMenuMessage build = SimpleMenuMessage.builder()
                    .message(consumerBundleResponse.getMessage())
                    .menuList(failedMenu)
                    .build();
            telegramBotApi.executeSendMessage(build, BotUtil.getChatId(update));
            return null;
        }
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return REQUEST_MESSAGE_COMMAND;
    }

    public static String getCommandMenu() {
        return REQUEST_MESSAGE_COMMAND;
    }
}