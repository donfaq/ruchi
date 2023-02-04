package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordOutputService implements OutputService {
    private final MessageChannel textChannel;

    private final Logger log = LoggerFactory.getLogger(DiscordOutputService.class);

    @Autowired
    public DiscordOutputService(JDA jda, DiscordConfigProperties discordConfig) {
        log.info("Trying to reach specified channel");
        textChannel = jda.getTextChannelById(discordConfig.channelId());

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

        MessageCreateBuilder builder = new MessageCreateBuilder()
                .setContent(message.text());
        if (message.images() != null) {
            builder.setEmbeds(
                    message.images()
                            .stream()
                            .map(url -> new EmbedBuilder().setImage(url.toString()).build())
                            .toList()
            );
        }
        try (MessageCreateData createData = builder.build()) {
            log.info("Sending message to Discord channel");
            textChannel.sendMessage(createData).queue();
        }
        log.info("Message sent");
    }
}
