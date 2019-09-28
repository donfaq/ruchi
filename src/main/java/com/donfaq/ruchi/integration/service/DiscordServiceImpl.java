package com.donfaq.ruchi.integration.service;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class DiscordServiceImpl implements DiscordService {
    private final Logger log = LoggerFactory.getLogger(DiscordServiceImpl.class);

    private TextChannel textChannel;
    private Deque<String> sentMessages;

    @Autowired
    public DiscordServiceImpl(
            @Value("${discord.botToken}") final String botToken,
            @Value("${discord.channelId}") final long channelId
    ) throws LoginException, InterruptedException {
        textChannel = (TextChannel) new JDABuilder(AccountType.BOT)
                .setToken(botToken)
                .build()
                .awaitReady()
                .getGuildChannelById(ChannelType.TEXT, channelId);

        this.sentMessages = new ArrayDeque<>(10);
    }

    private boolean isSentEarlier(String message) {
        return sentMessages.contains(message);
    }

    @Override
    public void sendMessageToTextChannel(String message) {
        if (!isSentEarlier(message)) {
            log.info("Sending new message to Discord channel");
            textChannel.sendMessage(message).queue();
            sentMessages.add(message);
        } else {
            log.info("Message already been sent");
        }
    }
}
