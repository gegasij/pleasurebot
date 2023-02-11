package com.pleasurebot.core.bot.service.utils;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.*;
import com.pleasurebot.core.bot.model.AttachmentType;
import com.pleasurebot.core.bot.model.Attachment;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;

@UtilityClass
public class MediaUtil {
    public static ArrayList<Attachment> getAttachments(Update update) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        if (update.message().document() != null) {
            attachments.add(documentConverter(update.message().document()));
        }
        if (update.message().photo() != null) {
            attachments.add(photoSizeConverter(update.message().photo()));
        }
        if (update.message().audio() != null) {
            attachments.add(audioConverter(update.message().audio()));
        }
        if (update.message().voice() != null) {
            attachments.add(voiceConverter(update.message().voice()));
        }
        return attachments;
    }

    public static AbstractMultipartRequest<?> getMediaMessage(Attachment attachment, Long chatId, String caption) {
        if (attachment.getAttachmentType().equals(AttachmentType.DOCUMENT.getValue())) {
            SendDocument sendDocument = new SendDocument(chatId, attachment.getTelegramFileId());
            if (caption != null) sendDocument.caption(caption);
            return sendDocument;
        }
        if (attachment.getAttachmentType().equals(AttachmentType.PHOTO.getValue())) {
            SendPhoto sendPhoto = new SendPhoto(chatId, attachment.getTelegramFileId());
            if (caption != null) sendPhoto.caption(caption);
            return sendPhoto;

        }
        if (attachment.getAttachmentType().equals(AttachmentType.AUDIO.getValue())) {
            SendAudio sendAudio = new SendAudio(chatId, attachment.getTelegramFileId());
            if (caption != null) sendAudio.caption(caption);
            return sendAudio;
        }
        if (attachment.getAttachmentType().equals(AttachmentType.VOICE.getValue())) {
            SendVoice sendVoice = new SendVoice(chatId, attachment.getTelegramFileId());
            if (caption != null) sendVoice.caption(caption);
            return sendVoice;
        }
        return null;
    }

    private static Attachment voiceConverter(Voice object) {
        return Attachment.builder()
                .telegramFileId(object.fileId())
                .attachmentType(AttachmentType.VOICE.getValue())
                .build();
    }

    private static Attachment photoSizeConverter(PhotoSize[] objects) {
        PhotoSize object = objects[objects.length - 1];
        return Attachment.builder()
                .telegramFileId(object.fileId())
                .attachmentType(AttachmentType.PHOTO.getValue())
                .build();
    }

    private static Attachment audioConverter(Audio object) {
        return Attachment.builder()
                .telegramFileId(object.fileId())
                .attachmentType(AttachmentType.AUDIO.getValue())
                .build();
    }

    private static Attachment documentConverter(Document document) {
        return Attachment.builder()
                .telegramFileId(document.fileId())
                .attachmentType(AttachmentType.DOCUMENT.getValue())
                .build();
    }

    public static boolean isAnyMedia(Update update) {
        Message message;
        if (update.callbackQuery() != null) {
            message = update.callbackQuery().message();
        } else message = update.message();
        return message.photo() != null ||
                message.voice() != null ||
                message.document() != null ||
                message.audio() != null;
    }
}
