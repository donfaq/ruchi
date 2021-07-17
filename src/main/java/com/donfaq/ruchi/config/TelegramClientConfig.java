package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import com.donfaq.ruchi.component.telegram.TgUpdatesListener;
import com.donfaq.ruchi.config.properties.TelegramConfigProperties;
import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramClientConfig {
    private final TelegramConfigProperties properties;
    private final TextGeneratorCommand textGeneratorCommand;

    @Bean
    public TelegramBot createTelegramBot() {
        log.info("creating telegram client");

        TelegramBot bot = new TelegramBot(this.properties.getBotToken());
        bot.setUpdatesListener(new TgUpdatesListener(bot, textGeneratorCommand));
        return bot;
    }

}
