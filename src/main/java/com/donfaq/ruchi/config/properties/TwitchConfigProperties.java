package com.donfaq.ruchi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Validated
@ConfigurationProperties("twitch")
public class TwitchConfigProperties {

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    @NotBlank
    private String botOAuthToken;

    @NotBlank
    private String channelName;
}


