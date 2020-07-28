package com.donfaq.ruchi.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;


@Configuration
class OAuth2ClientConfig {

    @Bean
    public OAuth2RestTemplate twitchOauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        return new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext(atr));
    }
}
