package com.donfaq.ruchi.model.vk.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallpostAttachment {

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("photo")
    private Photo photo;

    @JsonProperty("type")
    private String type;
}
