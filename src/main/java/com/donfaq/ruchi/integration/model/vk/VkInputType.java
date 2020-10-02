package com.donfaq.ruchi.integration.model.vk;

import com.donfaq.ruchi.integration.model.InputType;
import com.donfaq.ruchi.integration.model.vk.api.Wallpost;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkInputType implements InputType {
    private String type;

    @JsonProperty("group_id")
    private String groupId;

    private Wallpost object;

    private String secret;
}
