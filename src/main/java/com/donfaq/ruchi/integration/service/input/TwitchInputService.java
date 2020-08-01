package com.donfaq.ruchi.integration.service.input;

import com.donfaq.ruchi.integration.model.twitch.webhook.websub.WebSubHubMode;
import com.donfaq.ruchi.integration.model.twitch.webhook.websub.WebSubSubscriptionRequest;
import com.donfaq.ruchi.integration.model.twitch.webhook.websub.WebSubSubscriptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchInputService {

    private final @Qualifier("twitchOauth2RestTemplate")
    OAuth2RestOperations restTemplate;

    @Value("${security.oauth2.client.clientId}")
    private String twitchClientId;

    @Value("${app.url}")
    private String appUrl;

    @Value("${twitch.userId}")
    private String twitchUserId;

    @EventListener
    public void registerWebhook(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Sending Twitch webhook subscription request");

        WebSubSubscriptionRequest webSubSubscriptionRequest = new WebSubSubscriptionRequest();
        webSubSubscriptionRequest.setHubCallback(appUrl + "/twitch");
        webSubSubscriptionRequest.setHubMode(WebSubHubMode.SUBSCRIBE);
        webSubSubscriptionRequest.setHubTopic("https://api.twitch.tv/helix/streams?user_id=" + twitchUserId);
        webSubSubscriptionRequest.setHubLeaseSeconds(864000);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Client-ID", this.twitchClientId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WebSubSubscriptionRequest> request = new HttpEntity<>(webSubSubscriptionRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://api.twitch.tv/helix/webhooks/hub", request, String.class);

        if (!responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            throw new IllegalStateException(
                    "Twitch webhook subscription request failed with response: " + responseEntity.getBody());
        }
        log.info("Successfully send Twitch webhook subscription request");
    }

    public String process(WebSubSubscriptionResponse response) {
        log.info("Twitch response: {}", response.toString());
        return response.getHubChallenge();
    }

}
