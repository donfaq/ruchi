package com.donfaq.ruchi.component.discord;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactToSlashCommand extends ListenerAdapter {
    private final TextGeneratorCommand textGen;

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
        event.deferReply().queue();
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
                default -> "Я такое не умею :(";
            };
        } catch (Exception e) {
            log.warn("Exception during response generation", e);
        }
        event.getHook().sendMessage(responseMessage).queue();
    }

}
