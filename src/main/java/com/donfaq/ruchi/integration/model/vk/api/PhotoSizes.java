package com.donfaq.ruchi.integration.model.vk.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

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

    public PhotoSizes() {
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "PhotoSizes{" +
                "url=" + url +
                ", type='" + type + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
