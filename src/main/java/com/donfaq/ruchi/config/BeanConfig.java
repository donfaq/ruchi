package com.donfaq.ruchi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class BeanConfig {
    private final String twitchClientId;
    private final String twitchClientSecret;
    private final String twitchBotOAuthToken;

    public BeanConfig(
            @Value("${twitch.clientId}") String twitchClientId,
            @Value("${twitch.clientSecret}") String twitchClientSecret,
            @Value("${twitch.botOAuthToken}") String twitchBotOAuthToken
    ) {
        this.twitchBotOAuthToken = twitchBotOAuthToken;
        this.twitchClientId = twitchClientId;
        this.twitchClientSecret = twitchClientSecret;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public VkApiClient vkApiClient() {
        //TODO: Consider using custom TransportClient (using RestTemplate or WebClient) for VkApiClient
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient);
    }

    @Bean
    public DefaultUriBuilderFactory defaultUriBuilderFactory() {
        return new DefaultUriBuilderFactory();
    }


    @Bean
    public TwitchClient configureTwitchClient() {
        return TwitchClientBuilder
                .builder()
                .withClientId(this.twitchClientId)
                .withClientSecret(this.twitchClientSecret)
                .withEnableHelix(true)
                .withChatAccount(new OAuth2Credential("twitch", this.twitchBotOAuthToken))
                .withEnableChat(true)
                .build();
    }

}
