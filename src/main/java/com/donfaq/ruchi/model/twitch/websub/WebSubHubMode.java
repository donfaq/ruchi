package com.donfaq.ruchi.model.twitch.websub;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WebSubHubMode {
    @JsonProperty("subscribe")
    SUBSCRIBE,
    @JsonProperty("unsubscribe")
    UNSUBSCRIBE,
    @JsonProperty("denied")
    DENIED
}
