package com.pleasurebot.core.bot.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.bot.command.CommandHandler;
import com.pleasurebot.core.bot.model.BasicBundle;
import com.pleasurebot.core.bot.model.message.SimpleMenuMessage;
import com.pleasurebot.core.bot.repository.AttachmentRepository;
import com.pleasurebot.core.bot.repository.BundleJpaRepository;
import com.pleasurebot.core.bot.service.utils.CallbackDataParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BundleEditMenuHandler implements CommandHandler {
    private static final String BUNDLE_EDIT_MENU_COMMAND = "bundleEditMenu";
    private final Pair<String, String> DELETE = Pair.of("удалить", DeleteBundleHandler.getMenuCommand());
    private final Pair<String, String> BACK = Pair.of("Назад", BundlePageHandler.getCommandMenu(0));
    private final List<Pair<String, String>> ACTION_MENU = List.of(BACK, DELETE);
    private final BundleJpaRepository bundleJpaRepository;
    private final AttachmentRepository attachmentRepository;

    public SimpleMenuMessage handleCommand(Update update) {
        String data = update.callbackQuery().data();

        String startBundleCommand = CallbackDataParser.getCommand(data);
        String parameter1 = CallbackDataParser.getParameter1(data);
        if (Objects.equals(startBundleCommand, BUNDLE_EDIT_MENU_COMMAND)) {
            if (NumberUtils.isCreatable(parameter1)) {
                int bundleId = NumberUtils.toInt(parameter1);
                return sendEditActionMenu(bundleId);
            }
        }
        return null;
    }

    @Override
    public Boolean checkAccessToCommand(Update update) {
        return true;
    }

    @Override
    public String getCommand() {
        return BUNDLE_EDIT_MENU_COMMAND;
    }

    public static String getMenuCommand() {
        return BUNDLE_EDIT_MENU_COMMAND;
    }

    private SimpleMenuMessage sendEditActionMenu(Integer bundleId) {
        Optional<BasicBundle> byId = bundleJpaRepository.findById(bundleId);
        if (byId.isPresent()) {
            SimpleMenuMessage.SimpleMenuMessageBuilder<?, ?> builder = SimpleMenuMessage.builder();

            Optional.of(attachmentRepository.findByBundleId(bundleId))
                    .filter(it -> !it.isEmpty())
                    .ifPresent(builder::attachments);

            Optional.ofNullable(byId.get().getMessage())
                    .ifPresentOrElse(
                            it -> builder.message("Сообщение: " + it),
                            () -> builder.message("[media]")
                    );

            return builder.menuList(List.of(BACK,
                            Pair.of(
                                    DELETE.getFirst(),
                                    DELETE.getSecond().concat(" ").concat(byId.get().getId().toString())))
                    )
                    .build();

        }
        return null;
    }
}
