package com.donfaq.ruchi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("discord")
public record DiscordConfigProperties(@NotBlank String botToken, @NotBlank String channelId, @NotBlank String guildId) {
}
