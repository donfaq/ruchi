package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import com.donfaq.ruchi.component.telegram.TgUpdatesListener;
import com.donfaq.ruchi.config.properties.TelegramConfigProperties;
import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramClientConfig {
    private static final Logger log = LoggerFactory.getLogger(TelegramClientConfig.class);

    @Autowired
    public TelegramClientConfig(TelegramConfigProperties properties, TextGeneratorCommand textGeneratorCommand) {
        this.properties = properties;
        this.textGeneratorCommand = textGeneratorCommand;
    }

    private final TelegramConfigProperties properties;
    private final TextGeneratorCommand textGeneratorCommand;

    @Bean
    public TelegramBot createTelegramBot() {
        log.info("creating telegram client");

        TelegramBot bot = new TelegramBot(this.properties.botToken());
        bot.setUpdatesListener(new TgUpdatesListener(bot, textGeneratorCommand));
        return bot;
    }

}
