package com.pleasurebot.core.implementation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pleasurebot.core.implementation.model.BotImpl;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class BotConfiguration {
    @SneakyThrows
    @Bean
    public BotImpl botImplementation() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("src/main/resources/config.json"), BotImpl.class);
    }

    @Bean(value = "implementation")
    public TelegramBot telegramBot(BotImpl bot) {
        String botToken = bot.getBotToken();
        return new TelegramBot(botToken);
    }

}
