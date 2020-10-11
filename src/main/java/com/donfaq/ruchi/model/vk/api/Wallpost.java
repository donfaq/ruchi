package com.donfaq.ruchi.model.vk.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallpost {
    /**
     * Access key to private object
     */
    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("attachments")
    private List<WallpostAttachment> attachments;

    /**
     * Date of publishing in Unixtime
     */
    @JsonProperty("date")
    private Integer date;

    /**
     * Date of editing in Unixtime
     */
    @JsonProperty("edited")
    private Integer edited;

    /**
     * Post author ID
     */
    @JsonProperty("from_id")
    private Integer fromId;

    /**
     * Post ID
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Is post archived, only for post owners
     */
    @JsonProperty("is_archived")
    private Boolean isArchived;

    /**
     * Information whether the post in favorites list
     */
    @JsonProperty("is_favorite")
    private Boolean isFavorite;

    /**
     * Wall owner's ID
     */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /**
     * Post signer ID
     */
    @JsonProperty("signer_id")
    private Integer signerId;

    /**
     * Post text
     */
    @JsonProperty("text")
    private String text;
}
