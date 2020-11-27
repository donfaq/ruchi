package com.donfaq.ruchi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties("app")
public class AppConfigProperties {

    /**
     * Public URL of the app for links, messages etc.
     */
    @NotBlank
    private String url;
}
