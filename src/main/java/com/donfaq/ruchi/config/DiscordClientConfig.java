package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.discord.DiscordReadyListener;
import com.donfaq.ruchi.component.discord.SlashCommandListener;
import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DiscordClientConfig {
    private final DiscordConfigProperties discordConfig;
    private final SlashCommandListener slashCommandListener;
    private final DiscordReadyListener discordReadyListener;

    @Bean
    public JDA configureDiscordClient() throws LoginException, InterruptedException {
        log.info("Initializing Discord client");
        return JDABuilder
                .createDefault(this.discordConfig.getBotToken())
                .addEventListeners(
                        slashCommandListener,
                        discordReadyListener
                )
                .build()
                .awaitReady();
    }
}
