package com.donfaq.ruchi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties("discord")
public class DiscordConfigProperties {

    @NotBlank
    private String botToken;

    @NotBlank
    private String channelId;

    @NotBlank
    private String guildId;
}
