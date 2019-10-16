package com.donfaq.ruchi.integration.model.vk;

import com.donfaq.ruchi.integration.model.InputType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Callback implements InputType {
    private String type;
    private String groupId;
    private Wallpost object;


    public Callback() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("group_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Wallpost getObject() {
        return object;
    }

    public void setObject(Wallpost object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "CallbackData{" +
                "type='" + type + '\'' +
                ", groupId='" + groupId + '\'' +
                ", object='" + object + '\'' +
                '}';
    }
}
