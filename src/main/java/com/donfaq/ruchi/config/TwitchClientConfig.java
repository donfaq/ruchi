package com.donfaq.ruchi.config;

import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitchClientConfig {
    private final TwitchConfigProperties twitchConfig;

    @Autowired
    public TwitchClientConfig(TwitchConfigProperties twitchConfig) {
        this.twitchConfig = twitchConfig;
    }

    @Bean
    public TwitchClient configureTwitchClient() {
        TwitchClient twitchClient = TwitchClientBuilder
                .builder()

                .withClientId(this.twitchConfig.clientId())
                .withClientSecret(this.twitchConfig.clientSecret())
                .withEnableHelix(true)

                .withChatAccount(
                        new OAuth2Credential("twitch", this.twitchConfig.botOAuthToken()))
                .withEnableChat(true)
                .withCommandTrigger("!")

                .build();

        twitchClient.getClientHelper().enableStreamEventListener(this.twitchConfig.channelName());
        twitchClient.getChat().joinChannel(this.twitchConfig.channelName());

        return twitchClient;
    }

}
