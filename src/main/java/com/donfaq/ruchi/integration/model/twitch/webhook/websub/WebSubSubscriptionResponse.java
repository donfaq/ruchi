package com.donfaq.ruchi.integration.model.twitch.webhook.websub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.LowerDotCaseStrategy.class)
public class WebSubSubscriptionResponse {
    WebSubHubMode hubMode;

    String hubTopic;

    String hubChallenge;

    String hubReason;

    @JsonProperty("hub.lease_seconds")
    Integer hubLeaseSeconds;

}
