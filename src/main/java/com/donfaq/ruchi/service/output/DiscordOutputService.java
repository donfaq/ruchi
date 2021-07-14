package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiscordOutputService implements OutputService {
    private final TextChannel textChannel;

    public DiscordOutputService(JDA jda, DiscordConfigProperties discordConfig) {
        log.info("Trying to reach specified channel");
        textChannel = (TextChannel) jda.getGuildChannelById(ChannelType.TEXT, discordConfig.getChannelId());

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
            action = action.setEmbeds(
                    message.getImages()
                           .stream()
                           .map(url -> new EmbedBuilder().setImage(url.toString()).build())
                           .toList()
            );
            action = action.override(true);
        }
        log.info("Sending message to Discord channel");
        action.queue();
    }
}
