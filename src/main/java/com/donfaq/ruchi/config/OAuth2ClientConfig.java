package com.donfaq.ruchi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;

import java.io.IOException;
import java.util.Collections;


@Configuration
class OAuth2ClientConfig {

    @Value("${twitch.clientId}")
    private String twitchClientId;

    @Bean
    public OAuth2RestTemplate twitchOauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext(atr));
        restTemplate.setInterceptors(
                Collections.singletonList(new ClientIdHeaderInterceptor())
        );
        return restTemplate;
    }

    class ClientIdHeaderInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("Client-ID", twitchClientId);
            return execution.execute(request, body);
        }
    }
}
