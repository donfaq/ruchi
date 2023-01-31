package com.donfaq.ruchi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("vk")
public record VkConfigProperties(
        @NotBlank String confirmationCode,
        @NotBlank String serviceAccessToken,
        @NotBlank String triggerString
) {
}
