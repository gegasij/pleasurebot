package com.pleasurebot.core.bot.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.model.message.TelegramMenuMessage;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.bot.service.utils.BotUtil;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import com.pleasurebot.core.bot.service.utils.PeriodUtil;
import com.pleasurebot.core.bot.model.BasicBundleConfig;
import com.pleasurebot.core.bot.model.Role;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.model.message.CustomTelegramMenuMessage;
import com.pleasurebot.core.bot.repository.BundleConfigJpaRepository;
import com.pleasurebot.core.bot.service.BundleConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BundleConfigEditMenuHandler implements CommandHandler {
    private final UserService userService;
    private final BundleConfigService bundleConfigService;
    private final BundleConfigJpaRepository bundleConfigJpaRepository;
    private static final String BUNDLE_CONFIG_EDIT = "bundleConfigEdit";
    private static final Pair<String, String> BACK = Pair.of("назад", MessagesMenuHandler.getMenu());

    //без задержки
    //15 минут
    //1 час
    //3 часа
    //1 раз в день

    @Override
    public TelegramMenuMessage handleCommand(Update update) {

        if (checkAccessToCommand(update)) {
            User user = userService.getUser(BotUtil.getChatId(update)).get();
            BasicBundleConfig basicBundleConfig = bundleConfigService.getBasicBundleConfig(user.getId());
            if (update.callbackQuery() != null) {
                String parameter1 = CallbackDataParser.getParameter1(update.callbackQuery().data());
                if (parameter1 != null) {
                    switch (parameter1) {
                        case "period" -> {
                            Long first = PeriodUtil.getNext(basicBundleConfig.getFrequency()).getFirst();
                            basicBundleConfig.setFrequency(first);
                        }
                        case "order" -> basicBundleConfig.setRandomOrder(!basicBundleConfig.isRandomOrder());
                        case "used" -> basicBundleConfig.setAlwaysNew(!basicBundleConfig.isAlwaysNew());
                    }
                    bundleConfigJpaRepository.save(basicBundleConfig);
                }
            }
            return CustomTelegramMenuMessage.builder()
                    .message("Ваши настройки вывода сообщений партреру, нажмите на них чтобы изменить при необходимости")
                    .inlineKeyboardButtons(createMenuButtons(basicBundleConfig))
                    .build();
        }
        return null;
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return userService.isThisRole(update, Role.CLIENT);
    }

    @Override
    public String getCommand() {
        return BUNDLE_CONFIG_EDIT;
    }

    public static String getMenu() {
        return BUNDLE_CONFIG_EDIT;
    }

    private List<List<Pair<String, String>>> createMenuButtons(BasicBundleConfig bbc) {
        String frequency = PeriodUtil.getCurrent(bbc.getFrequency()).getSecond();
        Pair<String, String> frequencyButton = Pair.of(frequency, "%s period".formatted(BUNDLE_CONFIG_EDIT));

        String randomOrder = randomOrderReceiver(bbc.isRandomOrder());
        Pair<String, String> randomOrderButton = Pair.of( randomOrder,"%s order".formatted(BUNDLE_CONFIG_EDIT));

        String alwaysNew = alwaysNewReceiver(bbc.isAlwaysNew());
        Pair<String, String> alwaysNewButton = Pair.of(alwaysNew,"%s used".formatted(BUNDLE_CONFIG_EDIT));

        List<Pair<String, String>> row1Menu = List.of(frequencyButton);
        List<Pair<String, String>> row2Menu = List.of(randomOrderButton, alwaysNewButton);
        List<Pair<String, String>> row3Menu = List.of(BACK);

        return List.of(row1Menu, row2Menu, row3Menu);
    }

    private static String randomOrderReceiver(boolean b) {
        return b ? "в случайном порядке" : "по порядку";
    }

    private static String alwaysNewReceiver(boolean b) {
        return b ? "новые" : "любые";
    }

}
