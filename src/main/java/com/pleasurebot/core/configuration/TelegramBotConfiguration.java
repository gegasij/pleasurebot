package com.pleasurebot.core.configuration;


import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot("1886697492:AAGZWoW5hjAalqk3HtBfrfrWY_ItKvG-Ddc");
    }
}
