package com.donfaq.ruchi.integration.service.output;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
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

@Service
public class DiscordOutputService implements OutputService {
    private final Logger log = LoggerFactory.getLogger(DiscordOutputService.class);
    private TextChannel textChannel;

    @Autowired
    public DiscordOutputService(
            @Value("${discord.botToken}") final String botToken,
            @Value("${discord.channelId}") final long channelId
    ) throws LoginException, InterruptedException {
        log.info("Connecting to Discord bot");
        JDA jda = new JDABuilder(AccountType.BOT)
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
        log.info("DiscordOutputService ready to send messages");
    }


    @Override
    public void send(BroadcastMessage message) {
        log.info("Sending new message to Discord channel");
        textChannel.sendMessage(message.getFullText()).queue();
    }
}
