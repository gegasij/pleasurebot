package com.pleasurebot.core.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pleasurebot.core.bot.TelegramBotApi;
import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.message.CustomEditMenuMessage;
import com.pleasurebot.core.repository.BundlePagingRepository;
import com.pleasurebot.core.service.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaginationMenuService {

    private static final Pair<String, String> BACK = Pair.of("Назад", "client Bundle");
    private static final Pair<String, String> START = Pair.of("ᐊᐊᐊ", "start");
    private static final Pair<String, String> PREVIOUS = Pair.of("ᐊ", "previous");
    private static final Pair<String, String> NEXT = Pair.of("ᐅ", "next");
    private static final Pair<String, String> LAST = Pair.of("ᐅᐅᐅ", "last");
    private static final String CLIENT_BUNDLE_PREFIX = "client bundle ";

    private static final List<Pair<String, String>> PAGINATION_MENU = List.of(BACK,
            START,
            PREVIOUS,
            NEXT,
            LAST);


    private final BundlePagingRepository bundlePagingRepository;
    private final TelegramBotApi telegramBotApi;

    @Value("${defaultMenuColumns}")
    public Integer defaultMenuColumns;

    @Value("${defaultMenuRows}")
    public Integer defaultMenuRows;

    public boolean handleUpdate(Update update) {
        String data = update.callbackQuery().data();
        if (data == null) {
            return false;
        }
        String startBundleCommand = CallbackDataParser.parsePaginationCommand(data);

        Long messageId = update.callbackQuery().message().messageId().longValue();
        Long chatId = update.callbackQuery().message().chat().id();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        if (NumberUtils.isCreatable(startBundleCommand)) {
            int pageNumber = NumberUtils.toInt(startBundleCommand);
            PageRequest of = PageRequest.of(pageNumber, defaultMenuColumns * defaultMenuRows);
            Page<Bundle> all = bundlePagingRepository.findAll(of);
            List<InlineKeyboardButton> inlineKeyboardButtonRow = new ArrayList<>();
            for (int i = 0; i < all.getContent().size(); i++) {
                for (int j = 0; j < defaultMenuColumns && i < all.getContent().size(); j++) {
                    Bundle bundle = all.getContent().get(i);
                    inlineKeyboardButtonRow.add(new InlineKeyboardButton(bundle.getOrder() + ". " + bundle.getMessage())
                            .callbackData(CLIENT_BUNDLE_PREFIX+bundle.getId().toString()));
                    i++;
                }
                inlineKeyboardButtons.add(inlineKeyboardButtonRow);
                inlineKeyboardButtonRow = new ArrayList<>();
            }
            inlineKeyboardButtons.add(getPaginationMenu(all));
            InlineKeyboardButton[][] inlineKeyboardButtons1 = inlineKeyboardButtons.stream().map(it -> it.toArray(new InlineKeyboardButton[0]))
                    .toList().toArray(new InlineKeyboardButton[0][]);

            CustomEditMenuMessage messages = CustomEditMenuMessage.builder()
                    .inlineKeyboardButtons(inlineKeyboardButtons1)
                    .editedMessageId(messageId)
                    .chatId(chatId)
                    .message("you messages 123")
                    .build();

            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(update.callbackQuery().id());
            telegramBotApi.sendEditMenuMessage(messages);
            telegramBotApi.sendMessage(answerCallbackQuery);
        }
        return false;
    }

    private List<InlineKeyboardButton> getPaginationMenu(Page<Bundle> all) {
        int nextOrLastPage = all.nextOrLastPageable().getPageNumber();
        int previousOrFirstPage = all.previousOrFirstPageable().getPageNumber();
        int totalPages = all.getTotalPages();
        return List.of(new InlineKeyboardButton(BACK.getFirst()).callbackData("client"),
                new InlineKeyboardButton(START.getFirst()).callbackData("client bundle page 0"),
                new InlineKeyboardButton(PREVIOUS.getFirst()).callbackData("client bundle page " + previousOrFirstPage),
                new InlineKeyboardButton(NEXT.getFirst()).callbackData("client bundle page " + nextOrLastPage),
                new InlineKeyboardButton(LAST.getFirst()).callbackData("client bundle page " + (totalPages - 1))
        );
    }

}
