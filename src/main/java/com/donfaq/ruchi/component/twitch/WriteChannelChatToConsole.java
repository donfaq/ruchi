package com.donfaq.ruchi.component.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WriteChannelChatToConsole {

    private final Logger log = LoggerFactory.getLogger(WriteChannelChatToConsole.class);

    public WriteChannelChatToConsole(TwitchClient twitchClient) {
        SimpleEventHandler eventHandler = twitchClient
                .getEventManager()
                .getEventHandler(SimpleEventHandler.class);
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent event) {
        log.info("[#{}] @{}: {}", event.getChannel().getName(), event.getUser().getName(), event.getMessage());
    }

}
