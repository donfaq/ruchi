package com.donfaq.ruchi.integration.model.twitch.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TwitchUser {
    /**
     * User’s broadcaster type: "partner", "affiliate", or "".
     */
    private String broadcasterType;
    /**
     * User’s channel description.
     */
    private String description;
    /**
     * User’s display name.
     */
    private String displayName;
    /**
     * User’s email address. Returned if the request includes the user:read:email scope.
     */
    private String email;
    /**
     * User’s ID.
     */
    private String id;
    /**
     * User’s login name.
     */
    private String login;
    /**
     * URL of the user’s offline image.
     */
    private String offlineImageUrl;
    /**
     * URL of the user’s profile image.
     */
    private String profileImageUrl;
    /**
     * User’s type: "staff", "admin", "global_mod", or "".
     */
    private String type;
    /**
     * Total number of views of the user’s channel.
     */
    private String viewCount;

}
