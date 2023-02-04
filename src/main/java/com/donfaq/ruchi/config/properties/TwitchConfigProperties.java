package com.donfaq.ruchi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("twitch")
public record TwitchConfigProperties(@NotBlank String clientId, @NotBlank String clientSecret,
                                     @NotBlank String botOAuthToken, @NotBlank String channelName) {


}


