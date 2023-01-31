package com.donfaq.ruchi.component.discord;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SlashCommandListener extends ListenerAdapter {
    private final TextGeneratorCommand textGen;

    private final Logger log = LoggerFactory.getLogger(SlashCommandListener.class);

    @Autowired
    public SlashCommandListener(TextGeneratorCommand textGen) {
        this.textGen = textGen;
    }

    private String getQuery(SlashCommandEvent event) {
        OptionMapping queryOption = event.getOption("начало");
        if (Objects.isNull(queryOption)) {
            return "";
        } else {
            return queryOption.getAsString();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        event.deferReply().queue((commands) -> log.info("successfully marked slash command as noticed"), (throwable) -> log.error("error marking slash command as noticed", throwable));
        log.info(event.getName());

        String defaultResponse = "Что-то пошло не так";
        String responseMessage = defaultResponse;
        try {
            responseMessage = switch (event.getName()) {
                case "ping" -> "pong";
                case "speak" -> textGen.speak(getQuery(event)).orElse(defaultResponse);
                case "pron" -> textGen.pron(getQuery(event)).orElse(defaultResponse);
                case "gachi" -> textGen.gachi(getQuery(event)).orElse(defaultResponse);
                case "kalik" -> textGen.kalik(getQuery(event)).orElse(defaultResponse);
                case "woman" -> textGen.woman(getQuery(event)).orElse(defaultResponse);
                case "spam" -> textGen.spam(getQuery(event)).orElse(defaultResponse);
                case "vata" -> textGen.vata(getQuery(event)).orElse(defaultResponse);
                default -> "Я такое не умею :(";
            };
        } catch (Exception e) {
            log.warn("Exception during response generation", e);
        }
        event.getHook().sendMessage(responseMessage).queue((commands) -> log.info("successfully responded to slash command '{}'", event.getName()), (throwable) -> log.error("error during response to slash command '{}'", event.getName(), throwable));
    }

}
