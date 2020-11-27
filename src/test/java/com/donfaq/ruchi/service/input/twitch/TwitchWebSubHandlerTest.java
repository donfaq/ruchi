package com.donfaq.ruchi.service.input.twitch;

import com.donfaq.ruchi.config.properties.AppConfigProperties;
import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
import com.donfaq.ruchi.model.twitch.api.TwitchUser;
import com.donfaq.ruchi.model.twitch.api.TwitchWebhookSubscription;
import com.donfaq.ruchi.model.twitch.websub.WebSubHubMode;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TwitchWebSubHandlerTest {

    private TwitchWebSubHandler twitchWebSubHandler;
    private AppConfigProperties appProperties;
    private TwitchConfigProperties twitchProperties;
    private TwitchNotificationProcessor notificationProcessor;
    private ObjectMapper objectMapper;
    private TwitchApiService twitchApiService;

    @BeforeEach
    public void setUp() {
        appProperties = new AppConfigProperties();
        appProperties.setUrl("app-url");

        TwitchConfigProperties.Credentials credentials = new TwitchConfigProperties.Credentials();
        credentials.setUserLogin("testUserLogin");
        twitchProperties = new TwitchConfigProperties();
        twitchProperties.setCredentials(credentials);

        objectMapper = new ObjectMapper();
        notificationProcessor = Mockito.mock(TwitchNotificationProcessor.class);
        twitchApiService = Mockito.mock(TwitchApiService.class);

        twitchWebSubHandler = new TwitchWebSubHandler(
                appProperties,
                twitchProperties,
                notificationProcessor,
                objectMapper,
                twitchApiService,
                new DefaultUriBuilderFactory()
        );
    }

    @Test
    public void testWebSubSubscriptionResponse() {
        String hubChallenge = "test";
        WebSubSubscriptionResponse response = new WebSubSubscriptionResponse();
        response.setHubChallenge(hubChallenge);

        assertEquals(
                ResponseEntity.ok(hubChallenge),
                twitchWebSubHandler.handleWebSubSubscriptionResponse(response)
        );
    }

    @Test
    public void testPostConstructActions() {
        TwitchUser relatedUser = new TwitchUser();
        relatedUser.setId("123");
        when(twitchApiService.getUsers(any())).thenReturn(Optional.of(relatedUser));

        appProperties.setUrl("appUrl");
        twitchProperties.setBaseUrl("twitchUrl");

        String relatedCallback = String.format("%s/twitch", appProperties.getUrl());
        String differentCallback = "differentCallback";
        String relatedTopic = String.format("%s/streams?user_id=%s", twitchProperties.getBaseUrl(), relatedUser.getId());
        String differentTopic = "differentTopic";

        TwitchWebhookSubscription relatedSubscription = new TwitchWebhookSubscription();
        relatedSubscription.setCallback(relatedCallback);
        relatedSubscription.setTopic(relatedTopic);

        TwitchWebhookSubscription unrelatedSubscription1 = new TwitchWebhookSubscription();
        unrelatedSubscription1.setCallback(relatedCallback);
        unrelatedSubscription1.setTopic(differentTopic);

        TwitchWebhookSubscription unrelatedSubscription2 = new TwitchWebhookSubscription();
        unrelatedSubscription2.setCallback(differentCallback);
        unrelatedSubscription2.setTopic(relatedTopic);

        when(twitchApiService.getWebhookSubscriptions())
                .thenReturn(List.of(relatedSubscription, unrelatedSubscription1, unrelatedSubscription2));

        twitchWebSubHandler.updateWebhookSubscription();

        verify(twitchApiService, times(1))
                .getUsers(twitchProperties.getCredentials().getUserLogin());
        verify(twitchApiService, times(1))
                .sendWebhookSubscriptionRequest(
                        eq(relatedCallback),
                        eq(WebSubHubMode.UNSUBSCRIBE),
                        eq(relatedTopic),
                        anyString()
                );
        verify(twitchApiService, times(1)).getWebhookSubscriptions();
        verify(twitchApiService, times(1))
                .sendWebhookSubscriptionRequest(
                        eq(relatedCallback),
                        eq(WebSubHubMode.SUBSCRIBE),
                        eq(relatedTopic),
                        anyString()
                );
        verifyNoMoreInteractions(twitchApiService);
    }

}
