package com.donfaq.ruchi.integration.model.vk.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {
    /**
     * Access key for the photo
     */
    @JsonProperty("access_key")
    private String accessKey;

    /**
     * Album ID
     */
    @JsonProperty("album_id")
    private Integer albumId;

    /**
     * Date when uploaded
     */
    @JsonProperty("date")
    private Integer date;

    /**
     * Original photo height
     */
    @JsonProperty("height")
    private Integer height;

    /**
     * Photo ID
     */
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("images")
    private List<Image> images;

    /**
     * Latitude
     */
    @JsonProperty("lat")
    private Float lat;

    /**
     * Longitude
     */
    @JsonProperty("long")
    private Float lng;

    /**
     * Photo owner's ID
     */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /**
     * Post ID
     */
    @JsonProperty("post_id")
    private Integer postId;

    @JsonProperty("sizes")
    private List<PhotoSizes> sizes;

    /**
     * Photo caption
     */
    @JsonProperty("text")
    private String text;

    /**
     * ID of the user who have uploaded the photo
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * Original photo width
     */
    @JsonProperty("width")
    private Integer width;
}
