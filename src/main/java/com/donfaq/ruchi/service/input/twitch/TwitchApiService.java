package com.donfaq.ruchi.service.input.twitch;

import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
import com.donfaq.ruchi.model.twitch.api.TwitchGame;
import com.donfaq.ruchi.model.twitch.api.TwitchResponse;
import com.donfaq.ruchi.model.twitch.api.TwitchUser;
import com.donfaq.ruchi.model.twitch.api.TwitchWebhookSubscription;
import com.donfaq.ruchi.model.twitch.websub.WebSubHubMode;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(TwitchConfigProperties.class)
public class TwitchApiService {

    @Qualifier("twitchOauth2RestTemplate")
    private final OAuth2RestOperations restTemplate;
    private final UriBuilderFactory uriBuilderFactory;
    private final TwitchConfigProperties twitchProperties;

    private UriBuilder twitchUriBuilder() {
        return uriBuilderFactory.uriString(twitchProperties.getBaseUrl());
    }

    public Optional<TwitchUser> getUsers(String login) {
        log.info("Getting user information for login: {}", login);

        URI url = twitchUriBuilder().pathSegment("users").queryParam("login", login).build();
        ResponseEntity<TwitchResponse<TwitchUser>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchUser>>() {}
        );

        return Objects.requireNonNull(response.getBody()).getData().stream().findFirst();
    }

    public List<TwitchWebhookSubscription> getWebhookSubscriptions() {
        log.info("Getting Twitch webhook subscriptions");
        URI url = twitchUriBuilder().pathSegment("webhooks").pathSegment("subscriptions").build();
        ResponseEntity<TwitchResponse<TwitchWebhookSubscription>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchWebhookSubscription>>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    public Optional<TwitchGame> getGame(String gameId) {
        log.info("Getting information from Twitch for gameId={}", gameId);
        URI url = twitchUriBuilder().pathSegment("games").queryParam("id", gameId).build();
        ResponseEntity<TwitchResponse<TwitchGame>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchGame>>() {
                }
        );
        log.info("Result: {} {}", response.getStatusCodeValue(), response.getBody());
        return Objects.requireNonNull(response.getBody()).getData().stream().findFirst();
    }

    public void sendWebhookSubscriptionRequest(String callbackUrl, WebSubHubMode mode, String topic, String secret) {
        log.info("Sending Twitch webhook subscription request. Mode: {}; Topic: {};", mode, topic);
        WebSubSubscriptionRequest webSubSubscriptionRequest = new WebSubSubscriptionRequest();
        webSubSubscriptionRequest.setHubCallback(callbackUrl);
        webSubSubscriptionRequest.setHubMode(mode);
        webSubSubscriptionRequest.setHubTopic(topic);
        webSubSubscriptionRequest.setHubLeaseSeconds(864000);
        webSubSubscriptionRequest.setHubSecret(secret);

        URI url = twitchUriBuilder().pathSegment("webhooks").pathSegment("hub").build();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                url, webSubSubscriptionRequest, String.class);

        if (!responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            throw new IllegalStateException(
                    "Twitch webhook subscription request failed with response: " + responseEntity.getBody());
        }
        log.info("Successfully send Twitch webhook subscription request. Mode: {}; Topic: {}", mode, topic);
    }


}
