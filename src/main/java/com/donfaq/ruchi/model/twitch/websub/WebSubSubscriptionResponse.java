package com.donfaq.ruchi.model.twitch.websub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.LowerDotCaseStrategy.class)
public class WebSubSubscriptionResponse {
    WebSubHubMode hubMode;

    String hubTopic;

    String hubChallenge;

    String hubReason;

    @JsonProperty("hub.lease_seconds")
    Integer hubLeaseSeconds;

}
