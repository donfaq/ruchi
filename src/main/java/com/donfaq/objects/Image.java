package com.donfaq.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    public Image() {
    }

    /**
     * Height of the photo in px.
     */
    @JsonProperty("height")
    private Integer height;

    @JsonProperty("type")
    private String type;

    /**
     * Photo URL.
     */
    @JsonProperty("url")
    private URL url;

    /**
     * Width of the photo in px.
     */
    @JsonProperty("width")
    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Image{" +
                "height=" + height +
                ", type='" + type + '\'' +
                ", url=" + url +
                ", width=" + width +
                '}';
    }
}
