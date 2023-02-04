package com.donfaq.ruchi.component.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatToConsoleListener extends ListenerAdapter {

    private final Logger log = LoggerFactory.getLogger(ChatToConsoleListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        log.info("@{}: {}", event.getAuthor().getName(), event.getMessage().getContentRaw());
    }

}


