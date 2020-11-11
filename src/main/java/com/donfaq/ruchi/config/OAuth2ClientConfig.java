package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.RequestLoggingInterceptor;
import com.donfaq.ruchi.component.TwitchHeadersInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;

import java.util.List;


@Configuration
@RequiredArgsConstructor
class OAuth2ClientConfig {

    private final RequestLoggingInterceptor requestLoggingInterceptor;
    private final TwitchHeadersInterceptor twitchHeadersInterceptor;

    @Bean
    public OAuth2RestTemplate twitchOauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext(atr));
        restTemplate.setInterceptors(
                List.of(twitchHeadersInterceptor, requestLoggingInterceptor)
        );
        return restTemplate;
    }

}
