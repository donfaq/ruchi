package com.donfaq.ruchi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Validated
@ConfigurationProperties("model")
public class ModelConfigProperties {

    @NotBlank
    private String address;

    @Positive
    @NotNull
    private int port;
}
