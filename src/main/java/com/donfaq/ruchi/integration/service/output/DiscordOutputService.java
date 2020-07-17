package com.donfaq.ruchi.integration.service.output;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.net.URL;

@Slf4j
@Service
public class DiscordOutputService implements OutputService {
    private final TextChannel textChannel;

    @Autowired
    public DiscordOutputService(
            @Value("${discord.botToken}") final String botToken,
            @Value("${discord.channelId}") final long channelId
    ) throws LoginException, InterruptedException {
        log.info("Connecting to Discord bot");
        JDA jda = JDABuilder.createDefault(botToken)
                            .build()
                            .awaitReady();

        log.info("Trying to reach specified channel");
        textChannel = (TextChannel) jda.getGuildChannelById(ChannelType.TEXT, channelId);

        if (textChannel == null) {
            throw new InternalError("Can't find Discord channel by ID");
        }
        if (!textChannel.canTalk()) {
            throw new InternalError("Insufficient permissions in specified channel. Check Read and Write messages permissions");
        }
        log.info("DiscordOutputService ready to send messages");
    }


    @Override
    public void send(BroadcastMessage message) {
        log.info("Constructing new Discord message");

        MessageAction action = textChannel.sendMessage(message.getText());

        if (message.getImages() != null) {
            for (URL image : message.getImages()) {
                action = action.embed(
                        new EmbedBuilder().setImage(image.toString()).build()
                );
            }
            action = action.override(true);
        }

        log.info("Sending message to Discord channel");
        action.queue();
    }
}
