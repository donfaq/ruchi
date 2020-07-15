package com.donfaq.ruchi.integration.service.output;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.donfaq.ruchi.integration.model.vk.api.Image;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InputMediaPhoto;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMediaGroup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import jdk.internal.util.xml.impl.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Service
public class TelegramOutputService implements OutputService {

    private final TelegramBot bot;
    private final String tgChannelId;

    public TelegramOutputService(
            @Value("${telegram.botToken}") String tgBotToken,
            @Value("${telegram.channelId}") String tgChannelId
    ) {
        this.bot = new TelegramBot(tgBotToken);
        this.tgChannelId = tgChannelId;
    }

    @Override
    public void send(BroadcastMessage message) {

        if (message.getImages() != null) {
            SendPhoto request = new SendPhoto(tgChannelId, message.getImages().get(0).toString())
                    .caption(message.getText());

            bot.execute(request, new Callback<SendPhoto, SendResponse>() {
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
            SendMessage request = new SendMessage(tgChannelId, message.getText())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true);
            bot.execute(request, new Callback<SendMessage, SendResponse>() {
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
