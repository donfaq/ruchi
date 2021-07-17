package com.donfaq.ruchi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties("vk")
public class VkConfigProperties {
    @NotBlank
    private String confirmationCode;

    @NotBlank
    private String serviceAccessToken;

    @NotBlank
    private String triggerString;
}
