package com.donfaq.ruchi.integration.model.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WallpostAttachment {

    public WallpostAttachment() {
    }

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("photo")
    private Photo photo;

    @JsonProperty("type")
    private String type;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WallpostAttachment{" +
                "accessKey='" + accessKey + '\'' +
                ", photo=" + photo +
                ", type='" + type + '\'' +
                '}';
    }
}
