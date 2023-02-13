package com.pleasurebot.core.implementation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BotImpl {
    private String botToken;
    private List<AnswerMessage> answerMessage;
    private Payment payment;
}
