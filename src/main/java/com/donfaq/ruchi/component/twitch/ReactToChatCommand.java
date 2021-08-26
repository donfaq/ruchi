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
    private static final int MAX_LENGTH = 240;
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
            case "speak" -> event.respondToUser(textGeneratorCommand.speak(query,  MAX_LENGTH).orElse(defaultResponse));
            case "pron" -> event.respondToUser(textGeneratorCommand.pron(query, MAX_LENGTH).orElse(defaultResponse));
            case "gachi" -> event.respondToUser(textGeneratorCommand.gachi(query, MAX_LENGTH).orElse(defaultResponse));
            case "kalik" -> event.respondToUser(textGeneratorCommand.kalik(query, MAX_LENGTH).orElse(defaultResponse));
            case "woman" -> event.respondToUser(textGeneratorCommand.woman(query, MAX_LENGTH).orElse(defaultResponse));
            case "spam" -> event.respondToUser(textGeneratorCommand.spam(query, MAX_LENGTH).orElse(defaultResponse));
            default -> event.respondToUser("Я такое не умею :(");
        }
    }
}
