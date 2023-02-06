package com.pleasurebot.core.bot.command.client;

import com.pengrad.telegrambot.model.Update;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.bot.command.CommandUtil;
import com.pleasurebot.core.bot.command.StartHandler;
import com.pleasurebot.core.model.*;
import com.pleasurebot.core.model.message.SimpleMenuMessage;
import com.pleasurebot.core.model.message.TelegramMenuMessage;
import com.pleasurebot.core.repository.AttachmentRepository;
import com.pleasurebot.core.repository.BundleJpaRepository;
import com.pleasurebot.core.service.*;
import com.pleasurebot.core.service.utils.BotUtil;
import com.pleasurebot.core.service.utils.MediaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NewBundleHandler implements CommandHandler {
    private final UserService userService;
    private final BundleConfigService bundleConfigService;
    private final BundleService bundleService;
    private final BundleJpaRepository bundleJpaRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public TelegramMenuMessage handleCommand(Update update) {
        if (checkAccessToCommand(update)) {
            User user = userService.getUser(BotUtil.getChatId(update))
                    .orElseThrow(() -> new RuntimeException("cant dins user at " + this.getClass().getName() + " " + update));

            BasicBundleConfig byOwnerUserId = bundleConfigService.getBasicBundleConfig(user.getId());

            int nextOrderNumber = bundleService.getNextOrderNumber(byOwnerUserId.getId());

            BasicBundle bundle = new BasicBundle();
            bundle.setBundleConfigId(byOwnerUserId.getId());
            bundle.setOrder(nextOrderNumber);
            bundle.setMessage(BotUtil.getTextOrCaption(update));

            BasicBundle save = bundleJpaRepository.save(bundle);
            ArrayList<Attachment> attachments = MediaUtil.getAttachments(update);
            if (attachments.size() != 0) {
                attachments.forEach(it -> it.setBundleId(save.getId()));
                attachmentRepository.saveAll(attachments);
            }
            return SimpleMenuMessage.builder()
                    .message("<strong>Готово! </strong> Сообщение " + nextOrderNumber + " добавлено.")
                    .menuList(List.of(Pair.of("назад", StartHandler.getMenu())))
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
        return CommandUtil.NOT_COMMAND;
    }
}
