package com.donfaq;

import com.donfaq.objects.CallbackData;
import com.donfaq.objects.Wallpost;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayDeque;
import java.util.Deque;

public class DiscordClient {

    private static TextChannel discordChannel;
    private static Deque<Integer> sentMessages;

    public DiscordClient() {
        sentMessages = new ArrayDeque<>(5);

        try {
            String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
            long DISCORD_CHANNEL_ID = Long.parseLong(System.getenv("DISCORD_CHANNEL_ID"));
            discordChannel = (TextChannel) new JDABuilder(AccountType.BOT)
                    .setToken(DISCORD_BOT_TOKEN)
                    .build()
                    .getGuildChannelById(ChannelType.TEXT, DISCORD_CHANNEL_ID);
        } catch (Exception e) {
            throw new RuntimeException("Discord connection error");
        }
    }

    public void sendMessage(CallbackData data) {
        Wallpost wallpost = data.getObject();

        if (!sentMessages.contains(wallpost.getId())) {
            sentMessages.add(wallpost.getId());

            String text = wallpost.getText();
            if (text != null) {
                discordChannel.sendMessage(wallpost.getText()).queue();
            }
        }


    }

}
