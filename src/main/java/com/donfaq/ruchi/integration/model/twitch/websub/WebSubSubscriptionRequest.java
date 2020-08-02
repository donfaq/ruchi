package com.donfaq.ruchi.integration.model.twitch.websub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Subscription request follows WebSub specification:
 * https://www.w3.org/TR/websub/#subscriber-sends-subscription-request
 */
@Data
@JsonNaming(PropertyNamingStrategy.LowerDotCaseStrategy.class)
public class WebSubSubscriptionRequest {
    String hubCallback;

    WebSubHubMode hubMode;

    String hubTopic;

    @JsonProperty("hub.lease_seconds")
    Integer hubLeaseSeconds;

    String hubSecret;

}
