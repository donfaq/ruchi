package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.config.properties.TelegramConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramOutputService implements OutputService {

    private final TelegramBot bot;
    private final TelegramConfigProperties properties;

    @Override
    public void send(BroadcastMessage message) {

        if (message.getImages() != null) {
            SendPhoto request = new SendPhoto(
                    this.properties.getChannelId(), message.getImages().get(0).toString()
            ).caption(message.getText());

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
            SendMessage request = new SendMessage(this.properties.getChannelId(), message.getText())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true);
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
