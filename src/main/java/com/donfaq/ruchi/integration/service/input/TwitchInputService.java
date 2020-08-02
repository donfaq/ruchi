package com.donfaq.ruchi.integration.service.input;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.donfaq.ruchi.integration.model.twitch.api.*;
import com.donfaq.ruchi.integration.model.twitch.websub.WebSubHubMode;
import com.donfaq.ruchi.integration.model.twitch.websub.WebSubSubscriptionRequest;
import com.donfaq.ruchi.integration.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.integration.service.broadcast.BroadcastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchInputService {

    private final @Qualifier("twitchOauth2RestTemplate")
    OAuth2RestOperations restTemplate;

    private final BroadcastService broadcastService;

    @Value("${security.oauth2.client.clientId}")
    private String twitchClientId;

    @Value("${app.url}")
    private String appUrl;

    @Value("${twitch.userLogin}")
    private String twitchUserLogin;

    private TwitchUser twitchUser;

    @EventListener
    public void updateWebhookSubscription(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Updating Twitch webhook subscriptions");

        if (this.twitchUser == null) {
            this.twitchUser = getUsers(this.twitchUserLogin).orElseThrow();
        }

        getWebhookSubscriptions()
                .parallelStream()
                .forEach(subscription -> sendWebhookSubscriptionRequest(
                        WebSubHubMode.UNSUBSCRIBE, subscription.getTopic()
                ));

        sendWebhookSubscriptionRequest(
                WebSubHubMode.SUBSCRIBE,
                "https://api.twitch.tv/helix/streams?user_id=" + twitchUser.getId()
        );

    }

    private void sendWebhookSubscriptionRequest(WebSubHubMode mode, String topic) {
        log.info("Sending Twitch webhook subscription request. Mode: {}; Topic: {};", mode, topic);
        WebSubSubscriptionRequest webSubSubscriptionRequest = new WebSubSubscriptionRequest();
        webSubSubscriptionRequest.setHubCallback(appUrl + "/twitch");
        webSubSubscriptionRequest.setHubMode(mode);
        webSubSubscriptionRequest.setHubTopic(topic);
        webSubSubscriptionRequest.setHubLeaseSeconds(864000);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://api.twitch.tv/helix/webhooks/hub", webSubSubscriptionRequest, String.class);

        if (!responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            throw new IllegalStateException(
                    "Twitch webhook subscription request failed with response: " + responseEntity.getBody());
        }
        log.info("Successfully send Twitch webhook subscription request. Mode: {}; Topic: {}", mode, topic);
    }


    public String processWebhookSubscriptionResponse(WebSubSubscriptionResponse response) {
        log.info("Twitch response: {}", response.toString());
        return response.getHubChallenge();
    }

    public Optional<TwitchUser> getUsers(String login) {
        log.info("Getting user information for login: {}", login);

        ResponseEntity<TwitchResponse<TwitchUser>> response = restTemplate.exchange(
                "https://api.twitch.tv/helix/users?login={login}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchUser>>() {},
                login
        );
        log.info("Result: {} {}", response.getStatusCodeValue(), response.getBody());
        return response.getBody().getData().stream().findFirst();
    }

    public Optional<TwitchGame> getGame(String gameId) {
        log.info("Getting information from Twitch for gameId={}", gameId);
        ResponseEntity<TwitchResponse<TwitchGame>> response = restTemplate.exchange(
                "https://api.twitch.tv/helix/games?id={gameId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchGame>>() {},
                gameId
        );
        log.info("Result: {} {}", response.getStatusCodeValue(), response.getBody());
        return response.getBody().getData().stream().findFirst();
    }

    public List<TwitchWebhookSubscription> getWebhookSubscriptions() {
        log.info("Getting Twitch webhook subscriptions");
        ResponseEntity<TwitchResponse<TwitchWebhookSubscription>> response = restTemplate.exchange(
                "https://api.twitch.tv/helix/webhooks/subscriptions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchWebhookSubscription>>() {
                }
        );
        return response.getBody().getData();
    }

    public void processWebhookNotification(TwitchResponse<TwitchStream> body) {
        log.info("Processing new notification from Twitch webhook: {}", body);

        String messageText;
        if (body.getData() == null) {
            messageText = "Стрим ушёл в оффлайн! Всем пока";
        } else {
            TwitchStream stream = body.getData().stream().findFirst().orElseThrow();
            messageText = "СТРИМ ЖИВОЙ, ЗАХОДИТЕ ```" + stream.toString() + "```";
        }

        BroadcastMessage message = new BroadcastMessage();
        message.setText(messageText);
        broadcastService.broadcast(message);
    }
}
