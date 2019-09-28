package com.donfaq;

import com.donfaq.objects.CallbackData;
import com.donfaq.objects.Wallpost;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public class DiscordProcessor {
    private static final Logger log = LoggerFactory.getLogger(DiscordProcessor.class);

    private static Deque<Integer> sentMessages = new ArrayDeque<>(5);
    private static final String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
    private static final long  DISCORD_CHANNEL_ID = Long.parseLong(System.getenv("DISCORD_CHANNEL_ID"));

    private TextChannel discordChannel;

    public DiscordProcessor() {
        try {
            log.info("Initializing Discrod Client");
            discordChannel = (TextChannel) new JDABuilder(AccountType.BOT)
                    .setToken(DISCORD_BOT_TOKEN)
                    .build()
                    .awaitReady()
                    .getGuildChannelById(ChannelType.TEXT, DISCORD_CHANNEL_ID);
        } catch (Exception e) {
            throw new RuntimeException("Discord connection error");
        }
    }

    private boolean isContainsText(Wallpost wallpost) {
        return !"".equals(wallpost.getText()) && wallpost.getText() != null;
    }

    private boolean isContainsPhotoAttachment(Wallpost wallpost) {
        boolean result = false;

        if (wallpost.getAttachments() != null) {
            result = wallpost
                    .getAttachments()
                    .stream()
                    .anyMatch(attachment -> attachment.getType().equals("photo"));
        }
        return result;
    }

    private boolean isSentEarlier(Wallpost wallpost) {
        return sentMessages.contains(wallpost.getId());
    }

    private void addToSentMessages(Wallpost wallpost) {
        sentMessages.add(wallpost.getId());
    }

    private String getPhotoText(Wallpost wallpost) {
        return "";
    }

    private void sentMessageToTextChannel(String messageText) {
        discordChannel.sendMessage(messageText).queue();
    }

    public void processData(CallbackData data) {
        Wallpost wallpost = data.getObject();
        log.info("Processing data");

        if (!isSentEarlier(wallpost)) {

            String message = "";

            if (isContainsText(wallpost)) {
                message += wallpost.getText();
            }

            if (isContainsPhotoAttachment(wallpost)) {
                message = message + "\n\n" + getPhotoText(wallpost);
            }

            sentMessageToTextChannel(message);
            addToSentMessages(wallpost);

        }
    }


}
