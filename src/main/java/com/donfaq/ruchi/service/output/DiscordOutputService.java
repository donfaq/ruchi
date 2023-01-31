package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiscordOutputService implements OutputService {
    private final TextChannel textChannel;

    private final Logger log = LoggerFactory.getLogger(DiscordOutputService.class);

    public DiscordOutputService(JDA jda, DiscordConfigProperties discordConfig) {
        log.info("Trying to reach specified channel");
        textChannel = (TextChannel) jda.getGuildChannelById(ChannelType.TEXT, discordConfig.channelId());

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
        MessageAction action = textChannel.sendMessage(message.text());
        if (message.images() != null) {
            action = action.setEmbeds(
                    message.images()
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
