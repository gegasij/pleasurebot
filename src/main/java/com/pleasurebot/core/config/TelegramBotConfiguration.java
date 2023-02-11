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

    @Bean(name = "activatorBot")
    public TelegramBot activatorBot() {
        return new TelegramBot("5801400827:AAFxPUEZIcFyK5gR_Nszb6-ZTXqjUuegCkE");
    }
}
