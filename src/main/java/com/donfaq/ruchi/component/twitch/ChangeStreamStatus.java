package com.donfaq.ruchi.component.twitch;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.BroadcastService;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeStreamStatus {

    private final TwitchClient twitchClient;
    private final BroadcastService broadcastService;

    public ChangeStreamStatus(TwitchClient twitchClient, BroadcastService broadcastService) {
        this.twitchClient = twitchClient;
        this.broadcastService = broadcastService;

        SimpleEventHandler eventHandler = this.twitchClient
                .getEventManager()
                .getEventHandler(SimpleEventHandler.class);

        eventHandler.onEvent(ChannelGoLiveEvent.class, this::onGoLive);
        eventHandler.onEvent(ChannelGoOfflineEvent.class, this::onGoOffline);
    }

    private void onGoLive(ChannelGoLiveEvent event) {
        log.info("Received {}", event);
        sendMessageToChat(event.getChannel().getId(), "Всем привет! Поехали :3");

        String channelUrl = String.format("https://www.twitch.tv/%s", event.getStream().getUserLogin());
        String gameName = event.getStream().getGameName();
        sendNotification(switch (gameName) {
            case ("Just Chatting") -> String.format("Обкашливаем вопросики прямо сейчас. Смотреть и слушать вот тута: %s", channelUrl);
            default -> String.format("Стрим по игре %s только что начался! Жду тебя туть: %s", gameName, channelUrl);
        });
    }

    private void onGoOffline(ChannelGoOfflineEvent event) {
        log.info("Received {}", event);
        String messageText = "Стрим закончился! Спасибо всем, кто пришёл :3";
        sendMessageToChat(event.getChannel().getId(), messageText);
        sendNotification(messageText);
    }

    private void sendMessageToChat(String channelId, String message) {
        this.twitchClient.getChat().sendMessage(channelId, message);
    }

    private void sendNotification(String notificationText) {
        BroadcastMessage message = new BroadcastMessage();
        message.setText(notificationText);
        broadcastService.broadcast(message);
    }

}
