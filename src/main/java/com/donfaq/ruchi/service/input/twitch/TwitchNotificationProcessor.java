package com.donfaq.ruchi.service.input.twitch;

import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.model.twitch.api.TwitchResponse;
import com.donfaq.ruchi.model.twitch.api.TwitchStream;
import com.donfaq.ruchi.service.BroadcastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchNotificationProcessor {

    @Value("${twitch.userLogin}")
    private String TWITCH_USER_LOGIN;

    private final BroadcastService broadcastService;
    private final BlockingMemory memory;
    private final TwitchApiService twitchApiService;

    private Boolean isStreamOffline = Boolean.TRUE;

    private String constructBroadcastMessage(TwitchResponse<TwitchStream> notification) {
        String channelUrl = "https://www.twitch.tv/" + TWITCH_USER_LOGIN;
        String messageText;

        if (notification.getData().isEmpty()) {
            isStreamOffline = Boolean.TRUE;
            messageText = "Стрим закончился! Спасибо всем, кто пришёл :3";
        } else {
            TwitchStream stream = notification.getData().stream().findFirst().orElseThrow();
            String gameName = "Just Chatting";

            if (stream.getGameId() != null & !"".equals(stream.getGameId())) {
                gameName = twitchApiService.getGame(stream.getGameId()).orElseThrow().getName();
            }

            if (gameName.equals("Just Chatting")) {
                messageText = "Обкашливаем вопросики прямо сейчас: " + channelUrl;
            } else if (isStreamOffline) {
                messageText = "Стрим по " + gameName + " только что начался! Жду тебя туть: " + channelUrl;
            } else {
                messageText = "Теперь я стримлю " + gameName + ". Посмотреть можно прямо тут: " + channelUrl;
            }
            isStreamOffline = Boolean.FALSE;
        }
        return messageText;
    }

    public void process(TwitchResponse<TwitchStream> body) {
        BroadcastMessage message = new BroadcastMessage();
        message.setText(constructBroadcastMessage(body));

        if (this.memory.contains(message)) {
            log.info("Received Twitch notification that has already been processed: {}", body);
        }
        this.memory.add(message);
        broadcastService.broadcast(message);
    }



}
