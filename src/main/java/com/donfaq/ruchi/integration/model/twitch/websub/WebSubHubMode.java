package com.donfaq.ruchi.integration.model.twitch.websub;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WebSubHubMode {
    @JsonProperty("subscribe")
    SUBSCRIBE,
    @JsonProperty("unsubscribe")
    UNSUBSCRIBE,
    @JsonProperty("denied")
    DENIED
}
