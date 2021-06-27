package com.donfaq.ruchi.component.twitch;

import com.donfaq.ruchi.component.TextGeneratorClient;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.CommandEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReactToChatCommand {
    private final TextGeneratorClient textGeneratorClient;

    public ReactToChatCommand(TwitchClient twitchClient, TextGeneratorClient textGeneratorClient) {
        this.textGeneratorClient = textGeneratorClient;

        SimpleEventHandler eventHandler = twitchClient
                .getEventManager()
                .getEventHandler(SimpleEventHandler.class);
        eventHandler.onEvent(CommandEvent.class, this::onCommand);

    }

    private void onCommand(CommandEvent event) {
        log.info("Received CommandEvent: {}", event);

        String command;
        String query;
        String temp = event.getCommand().trim();
        int firstSpaceIndex = temp.indexOf(" ");
        if (firstSpaceIndex > 0) {
            command = temp.substring(0, firstSpaceIndex);
            query = temp.substring(firstSpaceIndex + 1);
        } else {
            command = temp.toLowerCase();
            query = null;
        }

        switch (command) {
            case "speak":
                event.getTwitchChat().sendMessage(
                        event.getSourceId(),
                        textGeneratorClient.generateText(query, 240)
                );
            case "ping":
                event.getTwitchChat().sendMessage(
                        event.getSourceId(),
                        "pong"
                );
            default:
                event.getTwitchChat().sendMessage(
                        event.getSourceId(),
                        "Я такое не умею :("
                );
        }
    }
}
