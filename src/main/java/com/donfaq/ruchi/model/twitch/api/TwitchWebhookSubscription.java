package com.donfaq.ruchi.model.twitch.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TwitchWebhookSubscription {
    /**
     * Date and time when this subscription expires. Encoded as RFC3339. The timezone is always UTC (“Z”).
     */
    private String expiresAt;
    /**
     * The topic used in the initial subscription.
     */
    private String topic;
    /**
     * The callback provided for this subscription.
     */
    private String callback;
}
