package com.pleasurebot.core.bot.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.bot.model.BasicBundle;
import com.pleasurebot.core.bot.model.BasicBundleConfig;
import com.pleasurebot.core.bot.model.Role;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.model.message.CustomTelegramMenuMessage;
import com.pleasurebot.core.bot.model.message.TelegramMenuMessage;
import com.pleasurebot.core.bot.repository.BundleConfigJpaRepository;
import com.pleasurebot.core.bot.repository.BundleJpaRepository;
import com.pleasurebot.core.bot.service.utils.BotUtil;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BundlePageHandler implements CommandHandler {
    private static final String BUNDLE_PAGE_COMMAND = "bundlePage";

    private static final Pair<String, String> BACK = Pair.of("Назад", ClientMenuHandler.getMenuCommand());
    private static final Pair<String, String> START = Pair.of("ᐊᐊᐊ", "start");
    private static final Pair<String, String> PREVIOUS = Pair.of("ᐊ", "previous");
    private static final Pair<String, String> NEXT = Pair.of("ᐅ", "next");
    private static final Pair<String, String> LAST = Pair.of("ᐅᐅᐅ", "last");

    private static final List<Pair<String, String>> PAGINATION_MENU = List.of(BACK,
            START,
            PREVIOUS,
            NEXT,
            LAST);


    private final BundleJpaRepository bundleJpaRepository;
    private final BundleConfigJpaRepository bundleConfigJpaRepository;
    private final UserService userService;

    @Value("${defaultMenuColumns}")
    public Integer defaultMenuColumns;

    @Value("${defaultMenuRows}")
    public Integer defaultMenuRows;

    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        String data = update.callbackQuery().data();
        String clientMenuCommand = CallbackDataParser.getCommand(data);
        if (checkAccessToCommand(update) && Objects.equals(clientMenuCommand, BUNDLE_PAGE_COMMAND)) {
            String parameter1 = CallbackDataParser.getParameter1(data);
            if (parameter1 != null) {
                return sendBundlePage(parameter1, update);
            }
        }
        return null;
    }

    private CustomTelegramMenuMessage sendBundlePage(String parameter1, Update update) {
        List<List<Pair<String, String>>> inlineKeyboardButtons = new ArrayList<>();
        Optional<User> user = userService.getUser(BotUtil.getChatId(update));
        user.get();
        BasicBundleConfig bundleConfig = bundleConfigJpaRepository.findByOwnerUserId(user.get().getId())
                .get();
        if (NumberUtils.isCreatable(parameter1)) {
            int pageNumber = NumberUtils.toInt(parameter1);
            PageRequest of = PageRequest.of(pageNumber, defaultMenuColumns * defaultMenuRows, Sort.by("order"));
            Page<BasicBundle> all = bundleJpaRepository.findAllByBundleConfigId(bundleConfig.getId(),of);
            wrapToMenuButtons(inlineKeyboardButtons, all);
            return CustomTelegramMenuMessage.builder()
                    .inlineKeyboardButtons(inlineKeyboardButtons)
                    .message("Ваши сообщения")
                    .build();
        }
        return null;
    }

    private void wrapToMenuButtons(List<List<Pair<String, String>>> inlineKeyboardButtons, Page<BasicBundle> all) {
        List<Pair<String, String>> inlineKeyboardButtonRow = new ArrayList<>();
        for (int i = 0; i < all.getContent().size(); ) {
            for (int j = 0; j < defaultMenuColumns && i < all.getContent().size(); j++) {
                BasicBundle bundle = all.getContent().get(i);
                String message;
                if (bundle.getMessage() == null) {
                    message = "[media]";
                } else message = bundle.getMessage();
                inlineKeyboardButtonRow.add(Pair.of("%d. %s".formatted(bundle.getOrder(), message), "%s %s".formatted(BundleEditMenuHandler.getMenuCommand(), bundle.getId().toString())));
                i++;
            }
            inlineKeyboardButtons.add(inlineKeyboardButtonRow);
            inlineKeyboardButtonRow = new ArrayList<>();
        }
        inlineKeyboardButtons.add(getPaginationMenu(all));
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return userService.isThisRole(update, Role.CLIENT);
    }

    @Override
    public String getCommand() {
        return BUNDLE_PAGE_COMMAND;
    }

    public static String getCommandMenu(int page) {
        return BUNDLE_PAGE_COMMAND + " " + page;
    }

    private List<Pair<String, String>> getPaginationMenu(Page<BasicBundle> all) {
        int nextOrLastPage = all.nextOrLastPageable().getPageNumber();
        int previousOrFirstPage = all.previousOrFirstPageable().getPageNumber();
        int totalPages = all.getTotalPages();
        return List.of(BACK,
                Pair.of(START.getFirst(), BundlePageHandler.getCommandMenu(0)),
                Pair.of(PREVIOUS.getFirst(), BundlePageHandler.getCommandMenu(previousOrFirstPage)),
                Pair.of(NEXT.getFirst(), BundlePageHandler.getCommandMenu(nextOrLastPage)),
                Pair.of(LAST.getFirst(), BundlePageHandler.getCommandMenu(totalPages - 1)));
    }

}
