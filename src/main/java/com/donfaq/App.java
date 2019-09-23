package com.donfaq;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@RestController
@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String VK_GROUP_CONFIRMATION_CODE = System.getenv("VK_GROUP_CONFIRMATION_CODE");
    private static final String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
    private static final Long DISCORD_CHANNEL_ID = Long.valueOf(System.getenv("DISCORD_CHANNEL_ID"));

    private static TextChannel discordChannel;
    private static JDA jda;

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/health")
    void health() {

    }

    @PostMapping("/callback")
    String callback(@RequestBody CallbackData callbackData) {
        String response = "ok";

        if (callbackData.getType().equals("confirmation")) {
            log.info("Confirmation request");
            response = VK_GROUP_CONFIRMATION_CODE;
        } else {
            log.info(callbackData.toString());
            discordChannel.sendMessage(callbackData.getObject()).queue();
        }
        return response;
    }


    private static JDA getJDA() throws LoginException {
        return new JDABuilder(AccountType.BOT).setToken(DISCORD_BOT_TOKEN).build();
    }


    public static void main(String[] args) {
        try {
            jda = getJDA().awaitReady();
            discordChannel = (TextChannel) jda.getGuildChannelById(ChannelType.TEXT, DISCORD_CHANNEL_ID);
        } catch (Exception e) {
            throw new RuntimeException("Discord connection error");
        }

        SpringApplication.run(App.class, args);


    }

}