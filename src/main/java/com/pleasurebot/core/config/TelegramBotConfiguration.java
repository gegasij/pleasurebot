package com.pleasurebot.core.config;


import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Bean(value = "lovelyBot")
    public TelegramBot lovelyBot() {
        return new TelegramBot("5941334455:AAGqnU3LDd-2el8a7vu4HAIJ0RAi91ApPZE");
    }
}
