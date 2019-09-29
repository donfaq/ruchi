package com.donfaq.ruchi.integration.service;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
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
    private JDA jda;
    private TextChannel textChannel;
    private Deque<String> sentMessages;

    @Autowired
    public DiscordServiceImpl(
            @Value("${discord.botToken}") final String botToken,
            @Value("${discord.channelId}") final long channelId
    ) throws LoginException, InterruptedException {
        log.info("Connecting to Discord bot");
        jda = new JDABuilder(AccountType.BOT)
                .setToken(botToken)
                .build()
                .awaitReady();

        log.info("Trying to reach specified channel");
        textChannel = (TextChannel) jda.getGuildChannelById(ChannelType.TEXT, channelId);

        if (textChannel == null) {
            throw new Error("Can't find Discord channel by ID");
        }
        if (!textChannel.canTalk()) {
            throw new Error("Insufficient permissions in specified channel. Check Read and Write messages permissions");
        }
        this.sentMessages = new ArrayDeque<>(10);
        log.info("Discord Service ready");
    }

    private synchronized boolean isSentEarlier(String message) {
        return sentMessages.contains(message);
    }

    private synchronized void markAsSent(String message) {
        sentMessages.add(message);
    }

    @Override
    public void sendMessageToTextChannel(String message) {
        if (!isSentEarlier(message)) {
            log.info("Sending new message to Discord channel");
            textChannel.sendMessage(message).queue();
            markAsSent(message);
        } else {
            log.info("Message already been sent");
        }
    }
}
