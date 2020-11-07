package com.donfaq.ruchi.service.input;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.model.twitch.api.*;
import com.donfaq.ruchi.model.twitch.websub.WebSubHubMode;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionRequest;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.service.BroadcastService;
import com.donfaq.ruchi.component.BlockingMemory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchInputService {

    private final @Qualifier("twitchOauth2RestTemplate")
    OAuth2RestOperations restTemplate;

    private final BroadcastService broadcastService;

    private final BlockingMemory memory;

    private final ObjectMapper objectMapper;

    @Value("${app.url}")
    private String HEROKU_APP_URL;

    @Value("${twitch.userLogin}")
    private String TWITCH_USER_LOGIN;

    @Value("${twitch.pauseBetweenNotifications}")
    private long PAUSE_BETWEEN_NOTIFICATIONS;

    private TwitchUser twitchUser;

    private Boolean isStreamOffline = Boolean.TRUE;

    private String subscriptionSecret;

    private Instant lastNotificationTime;

    private synchronized String getSubscriptionSecret() {
        if (subscriptionSecret == null) {
            subscriptionSecret = String.valueOf(UUID.randomUUID());
        }
        return subscriptionSecret;
    }

    @PostConstruct
    public void updateWebhookSubscription() {
        log.info("Updating Twitch webhook subscriptions");

        if (this.twitchUser == null) {
            this.twitchUser = getUsers(this.TWITCH_USER_LOGIN).orElseThrow();
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
        webSubSubscriptionRequest.setHubCallback(HEROKU_APP_URL + "/twitch");
        webSubSubscriptionRequest.setHubMode(mode);
        webSubSubscriptionRequest.setHubTopic(topic);
        webSubSubscriptionRequest.setHubLeaseSeconds(864000);
        webSubSubscriptionRequest.setHubSecret(getSubscriptionSecret());

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
                new ParameterizedTypeReference<TwitchResponse<TwitchUser>>() {
                },
                login
        );
        log.info("Result: {} {}", response.getStatusCodeValue(), response.getBody());
        return Objects.requireNonNull(response.getBody()).getData().stream().findFirst();
    }

    public Optional<TwitchGame> getGame(String gameId) {
        log.info("Getting information from Twitch for gameId={}", gameId);
        ResponseEntity<TwitchResponse<TwitchGame>> response = restTemplate.exchange(
                "https://api.twitch.tv/helix/games?id={gameId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<TwitchResponse<TwitchGame>>() {
                },
                gameId
        );
        log.info("Result: {} {}", response.getStatusCodeValue(), response.getBody());
        return Objects.requireNonNull(response.getBody()).getData().stream().findFirst();
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
        return Objects.requireNonNull(response.getBody()).getData();
    }


    private String constructBroadcastMessage(TwitchResponse<TwitchStream> notification) {
        String channelUrl = "https://www.twitch.tv/" + TWITCH_USER_LOGIN;
        String messageText;

        if (notification.getData().isEmpty()) {
            isStreamOffline = Boolean.TRUE;
            messageText = "Стрим закончился! Спасибо всем, кто пришёл :3";
        } else {
            TwitchStream stream = notification.getData().stream().findFirst().orElseThrow();
            String gameName = "Just Chatting";

            if (stream.getGameId() != null & !"".equals(stream.getGameId())) {
                gameName = getGame(stream.getGameId()).orElseThrow().getName();
            }

            if (gameName.equals("Just Chatting")) {
                messageText = "Обкашливаем вопросики прямо сейчас: " + channelUrl;
            } else if (isStreamOffline) {
                messageText = "Стрим по " + gameName + " только что начался! Жду тебя туть: " + channelUrl;
            } else {
                messageText = "Теперь я стримлю " + gameName + ". Посмотреть можно прямо тут: " + channelUrl;
            }
            isStreamOffline = Boolean.FALSE;
        }
        return messageText;
    }

    /**
     * Check that received request signature equals computed value
     *
     * @param payload   Request body as string
     * @param signature X-Hub-Signature header from POST request
     * @return true, if received signature is valid
     */
    @SneakyThrows
    public boolean validateSignature(String payload, String signature) {
        String computed = String.format(
                "sha256=%s",
                new HmacUtils(HmacAlgorithms.HMAC_SHA_256, getSubscriptionSecret()).hmacHex(payload)
        );

        log.info("Validating signature. Computed='{}'. Received='{}'", computed, signature);
        return MessageDigest.isEqual(signature.getBytes(), computed.getBytes());
    }

    @SneakyThrows
    public ResponseEntity<String> processWebhookNotification(String signature, String payload) {
        log.info("Processing new notification from Twitch: {}", payload);

        if (checkTimePolicy()) {
            TwitchResponse<TwitchStream> body = objectMapper.readValue(
                    payload, new TypeReference<TwitchResponse<TwitchStream>>() {});
            BroadcastMessage message = new BroadcastMessage();
            message.setText(constructBroadcastMessage(body));

            if (this.memory.contains(message)) {
                log.info("Received Twitch notification that has already been processed: {}", body);
                return ResponseEntity.ok().build();
            }
            this.memory.add(message);
            this.lastNotificationTime = Instant.now();
            broadcastService.broadcast(message);
        } else {
            log.warn("Twitch notification failed time policy check. Skipping.");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean checkTimePolicy() {
        Instant now = Instant.now();
        log.info("Checking time policy at {}. Previous notification at {}", now, this.lastNotificationTime);
        if (this.lastNotificationTime == null) return true;
        return ChronoUnit.MINUTES.between(this.lastNotificationTime, now) > PAUSE_BETWEEN_NOTIFICATIONS;
    }
}
