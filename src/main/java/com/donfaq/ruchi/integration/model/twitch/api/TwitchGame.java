package com.donfaq.ruchi.integration.model.twitch.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TwitchGame {
    /**
     * Template URL for the game’s box art.
     */
    private String boxArtUrl;
    /**
     * Game ID.
     */
    private String id;
    /**
     * Game name.
     */
    private String name;
}
