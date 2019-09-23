package com.donfaq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class CallbackData {
    private String type;
    private String groupId;
    Object object;


    public CallbackData() {
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

    @JsonRawValue
    public String getObject() {
        // default raw value: null or "[]"
        return this.object == null ? null : this.object.toString();
    }

    public void setObject(JsonNode node) {
        this.object = node;
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
