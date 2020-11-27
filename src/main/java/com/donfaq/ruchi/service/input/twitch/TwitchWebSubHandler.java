package com.donfaq.ruchi.service.input.twitch;

import com.donfaq.ruchi.config.properties.AppConfigProperties;
import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilderFactory;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({AppConfigProperties.class, TwitchConfigProperties.class})
public class TwitchWebSubHandler {

    private final AppConfigProperties appProperties;
    private final TwitchConfigProperties twitchProperties;
    private final TwitchNotificationProcessor twitchNotificationProcessor;
    private final ObjectMapper objectMapper;
    private final TwitchApiService twitchApiService;
    private final UriBuilderFactory uriBuilderFactory;

    private Instant lastNotificationTime;
    private TwitchUser twitchUser;
    private String subscriptionSecret;

    private String getSubscriptionCallbackUrl() {
        return uriBuilderFactory.uriString(appProperties.getUrl())
                                .pathSegment("twitch")
                                .build()
                                .toASCIIString();
    }

    private TwitchUser getTwitchUser() {
        if (this.twitchUser == null) {
            this.twitchUser = twitchApiService.getUsers(twitchProperties.getCredentials().getUserLogin()).orElseThrow();
        }
        return this.twitchUser;
    }

    private String getSubscriptionTopic() {
        return uriBuilderFactory
                .uriString(twitchProperties.getBaseUrl())
                .pathSegment("streams")
                .queryParam("user_id", getTwitchUser().getId())
                .build()
                .toASCIIString();
    }

    private synchronized String getSubscriptionSecret() {
        if (subscriptionSecret == null) {
            subscriptionSecret = String.valueOf(UUID.randomUUID());
        }
        return subscriptionSecret;
    }

    private void subscribe(String topic) {
        twitchApiService.sendWebhookSubscriptionRequest(
                getSubscriptionCallbackUrl(), WebSubHubMode.SUBSCRIBE, topic, getSubscriptionSecret());
    }

    private void unsubscribe(String topic) {
        twitchApiService.sendWebhookSubscriptionRequest(
                getSubscriptionCallbackUrl(), WebSubHubMode.UNSUBSCRIBE, topic, getSubscriptionSecret());
    }

    @PostConstruct
    public void updateWebhookSubscription() {
        String topic = getSubscriptionTopic();
        log.info("Updating Twitch WebSub subscription for topic '{}'", topic);
        twitchApiService.getWebhookSubscriptions()
                        .stream()
                        .filter(subscription -> Objects.equals(getSubscriptionCallbackUrl(), subscription.getCallback()) &&
                                Objects.equals(topic, subscription.getTopic()))
                        .map(TwitchWebhookSubscription::getTopic)
                        .forEach(this::unsubscribe);
        subscribe(topic);
    }


    private boolean checkTimePolicy() {
        Instant now = Instant.now();
        log.info("Checking time policy at {}. Previous notification at {}", now, this.lastNotificationTime);
        if (this.lastNotificationTime == null) return true;
        return ChronoUnit.MINUTES.between(this.lastNotificationTime, now) > twitchProperties.getPauseBetweenNotifications();
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
