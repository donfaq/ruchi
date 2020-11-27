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
@ConfigurationProperties("app.twitch")
public class TwitchConfigProperties {

    /**
     * Twitch API base URL
     */
    @NotBlank
    private String baseUrl = "https://api.twitch.tv/helix";

    /**
     * Number of minutes to wait before processing new websub notification
     */
    @Positive
    private long pauseBetweenNotifications;

    private Credentials credentials;

    @Validated
    @Getter
    @Setter
    public static class Credentials {
        @NotBlank
        private String userLogin;

        @NotBlank
        private String clientId;

        @NotBlank
        private String clientSecret;
    }


}


