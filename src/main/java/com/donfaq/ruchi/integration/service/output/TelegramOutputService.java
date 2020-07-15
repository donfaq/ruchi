package com.donfaq.ruchi.integration.service.output;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        SendMessage request = new SendMessage(tgChannelId, message.getText())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);

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
