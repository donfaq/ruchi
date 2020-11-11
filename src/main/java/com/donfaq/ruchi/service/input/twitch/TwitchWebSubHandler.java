package com.donfaq.ruchi.service.input.twitch;

import com.donfaq.ruchi.model.twitch.api.TwitchResponse;
import com.donfaq.ruchi.model.twitch.api.TwitchStream;
import com.donfaq.ruchi.model.twitch.api.TwitchUser;
import com.donfaq.ruchi.model.twitch.api.TwitchWebhookSubscription;
import com.donfaq.ruchi.model.twitch.websub.WebSubHubMode;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchWebSubHandler {
    @Value("${twitch.pauseBetweenNotifications}")
    private long PAUSE_BETWEEN_NOTIFICATIONS;
    @Value("${twitch.userLogin}")
    private String TWITCH_USER_LOGIN;
    @Value("${app.url}")
    private String HEROKU_APP_URL;

    private final TwitchNotificationProcessor twitchNotificationProcessor;
    private final ObjectMapper objectMapper;
    private final TwitchApiService twitchApiService;

    private Instant lastNotificationTime;
    private TwitchUser twitchUser;
    private String subscriptionSecret;

    private String getCallbackUrl() {
        return HEROKU_APP_URL + "/twitch";
    }

    private synchronized String getSubscriptionSecret() {
        if (subscriptionSecret == null) {
            subscriptionSecret = String.valueOf(UUID.randomUUID());
        }
        return subscriptionSecret;
    }

    private void subscribe(String topic) {
        twitchApiService.sendWebhookSubscriptionRequest(
                getCallbackUrl(), WebSubHubMode.SUBSCRIBE, topic, getSubscriptionSecret());
    }

    private void unsubscribe(String topic) {
        twitchApiService.sendWebhookSubscriptionRequest(
                getCallbackUrl(), WebSubHubMode.UNSUBSCRIBE, topic, getSubscriptionSecret());
    }

    @PostConstruct
    public void updateWebhookSubscription() {
        log.info("Updating Twitch webhook subscriptions");

        if (this.twitchUser == null) {
            this.twitchUser = twitchApiService.getUsers(this.TWITCH_USER_LOGIN).orElseThrow();
        }

        twitchApiService.getWebhookSubscriptions()
                        .parallelStream()
                        .map(TwitchWebhookSubscription::getTopic)
                        .forEach(this::unsubscribe);

        subscribe(String.format("https://api.twitch.tv/helix/streams?user_id=%s", twitchUser.getId()));
    }


    private boolean checkTimePolicy() {
        Instant now = Instant.now();
        log.info("Checking time policy at {}. Previous notification at {}", now, this.lastNotificationTime);
        if (this.lastNotificationTime == null) return true;
        return ChronoUnit.MINUTES.between(this.lastNotificationTime, now) > PAUSE_BETWEEN_NOTIFICATIONS;
    }

    public ResponseEntity<String> handleWebSubSubscriptionResponse(WebSubSubscriptionResponse response) {
        log.info("Twitch response: {}", response.toString());
        return ResponseEntity.ok(response.getHubChallenge());
    }

    @SneakyThrows
    public ResponseEntity<String> handleWebSubMessage(String payload) {
        log.info("Handling new notification from Twitch: {}", payload);

        if (checkTimePolicy()) {
            TwitchResponse<TwitchStream> body = objectMapper.readValue(
                    payload, new TypeReference<TwitchResponse<TwitchStream>>() {});
            this.lastNotificationTime = Instant.now();
            twitchNotificationProcessor.process(body);
        } else {
            log.warn("Twitch notification failed time policy check. Skipping.");
        }

        return ResponseEntity.ok().build();
    }
}
