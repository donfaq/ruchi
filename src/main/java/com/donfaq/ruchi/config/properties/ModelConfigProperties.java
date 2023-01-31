package com.donfaq.ruchi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@ConfigurationProperties("model")
public record ModelConfigProperties(@NotBlank String address, @Positive @NotNull int port) {
}
