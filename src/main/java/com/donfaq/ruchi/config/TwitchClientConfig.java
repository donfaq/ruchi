package com.donfaq.ruchi.config;

import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TwitchClientConfig {
    private final TwitchConfigProperties twitchConfig;

    @Bean
    public TwitchClient configureTwitchClient() {
        TwitchClient twitchClient = TwitchClientBuilder
                .builder()

                .withClientId(this.twitchConfig.getClientId())
                .withClientSecret(this.twitchConfig.getClientSecret())
                .withEnableHelix(true)

                .withChatAccount(
                        new OAuth2Credential("twitch", this.twitchConfig.getBotOAuthToken()))
                .withEnableChat(true)
                .withCommandTrigger("!")

                .build();

        twitchClient.getClientHelper().enableStreamEventListener(this.twitchConfig.getChannelName());
        twitchClient.getChat().joinChannel(this.twitchConfig.getChannelName());

        return twitchClient;
    }

}
