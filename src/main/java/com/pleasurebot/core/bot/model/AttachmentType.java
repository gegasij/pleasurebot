package com.pleasurebot.core.bot.model;

public enum AttachmentType {
    PHOTO(1), DOCUMENT(2), AUDIO(3), DICE(4), VOICE(5);
    private final int value;

    AttachmentType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
