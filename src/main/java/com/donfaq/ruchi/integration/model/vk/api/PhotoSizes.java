package com.donfaq.ruchi.integration.model.vk.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URL;

@Data
public class PhotoSizes {
    /**
     * URL of the image
     */
    @JsonProperty("url")
    private URL url;

    @JsonProperty("type")
    private String type;

    /**
     * Width in px
     */
    @JsonProperty("width")
    private Integer width;

    /**
     * Height in px
     */
    @JsonProperty("height")
    private Integer height;
}
