package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.config.properties.TelegramConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TelegramOutputService implements OutputService {

    private final TelegramBot bot;
    private final TelegramConfigProperties properties;

    private final Logger log = LoggerFactory.getLogger(TelegramOutputService.class);

    public TelegramOutputService(TelegramBot bot, TelegramConfigProperties properties) {
        this.bot = bot;
        this.properties = properties;
    }

    @Override
    public void send(BroadcastMessage message) {

        if (message.images() != null) {
            SendPhoto request = new SendPhoto(this.properties.channelId(), message.images().get(0).toString()).caption(message.text());

            bot.execute(request, new Callback<>() {
                @Override
                public void onResponse(SendPhoto request, SendResponse response) {
                    log.info("Message sent to Telegram");
                }

                @Override
                public void onFailure(SendPhoto request, IOException e) {
                    log.error("Error while sending message to Telegram", e);
                }
            });
        } else {
            SendMessage request = new SendMessage(this.properties.channelId(), message.text()).parseMode(ParseMode.HTML).disableWebPagePreview(true);
            bot.execute(request, new Callback<>() {
                @Override
                public void onResponse(SendMessage request, SendResponse response) {
                    log.info("Message sent to Telegram");
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    log.error("Error while sending message to Telegram", e);
                }
            });
        }


    }
}
