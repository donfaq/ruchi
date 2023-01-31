package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.discord.DiscordReadyListener;
import com.donfaq.ruchi.component.discord.SlashCommandListener;
import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class DiscordClientConfig {
    private final DiscordConfigProperties discordConfig;
    private final SlashCommandListener slashCommandListener;
    private final DiscordReadyListener discordReadyListener;
    private final Logger log = LoggerFactory.getLogger(DiscordClientConfig.class);

    @Autowired
    public DiscordClientConfig(DiscordConfigProperties discordConfig, SlashCommandListener slashCommandListener, DiscordReadyListener discordReadyListener) {
        this.discordConfig = discordConfig;
        this.slashCommandListener = slashCommandListener;
        this.discordReadyListener = discordReadyListener;
    }

    @Bean
    public JDA configureDiscordClient() throws LoginException, InterruptedException {
        log.info("Initializing Discord client");
        return JDABuilder.createDefault(this.discordConfig.botToken()).addEventListeners(slashCommandListener, discordReadyListener).build().awaitReady();
    }
}
