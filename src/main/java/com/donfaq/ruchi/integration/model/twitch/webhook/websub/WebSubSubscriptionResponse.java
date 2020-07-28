package com.donfaq.ruchi.integration.model.twitch.webhook.websub;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebSubSubscriptionResponse {
    WebSubHubMode hubMode;

    String hubTopic;

    String hubChallenge;

    String hubReason;

    @JsonProperty("hub.lease_seconds")
    Integer hubLeaseSeconds;

}
