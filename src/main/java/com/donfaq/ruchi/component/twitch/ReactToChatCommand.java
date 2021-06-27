package com.donfaq.ruchi.component.twitch;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.CommandEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReactToChatCommand {
    private final TextGeneratorCommand textGeneratorCommand;

    public ReactToChatCommand(TwitchClient twitchClient, TextGeneratorCommand textGeneratorCommand) {
        this.textGeneratorCommand = textGeneratorCommand;
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

        String defaultResponse = "Что-то пошло не так";
        switch (command) {
            case "ping" -> event.respondToUser("pong");
            case "speak" -> event.respondToUser(textGeneratorCommand.speak(query).orElse(defaultResponse));
            case "pron" -> event.respondToUser(textGeneratorCommand.pron(query).orElse(defaultResponse));
            case "gachi" -> event.respondToUser(textGeneratorCommand.gachi(query).orElse(defaultResponse));
            case "kalik" -> event.respondToUser(textGeneratorCommand.kalik(query).orElse(defaultResponse));
            default -> event.respondToUser("Я такое не умею :(");
        }
    }
}
