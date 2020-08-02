package com.donfaq.ruchi.integration.model.twitch.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TwitchStream {
    /**
     * ID of the game being played on the stream.
     */
    private String gameId;
    /**
     * Stream ID.
     */
    private String id;
    /**
     * Stream language.
     */
    private String language;
    /**
     * UTC timestamp.
     */
    private String startedAt;
    /**
     * Shows tag IDs that apply to the stream.
     */
    private List<String> tagIds;
    /**
     * Thumbnail URL of the stream. All image URLs have variable width and height.
     * You can replace {width} and {height} with any values to get that size image
     */
    private String thumbnailUrl;
    /**
     * Stream title.
     */
    private String title;
    /**
     * Stream type: "live" or "" (in case of error).
     */
    private String type;
    /**
     * ID of the user who is streaming.
     */
    private String userId;
    /**
     * Display name corresponding to user_id.
     */
    private String userName;
    /**
     * Number of viewers watching the stream at the time of the query.
     */
    private Integer viewerCount;
}
