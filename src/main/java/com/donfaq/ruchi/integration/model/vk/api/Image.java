package com.donfaq.ruchi.integration.model.vk.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.net.URL;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    /**
     * Height of the photo in px.
     */
    private Integer height;

    private String type;

    /**
     * Photo URL.
     */
    private URL url;

    /**
     * Width of the photo in px.
     */
    private Integer width;
}
