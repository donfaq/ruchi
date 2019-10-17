package com.donfaq.ruchi.integration.model.vk;

import com.donfaq.ruchi.integration.model.BroadcastMessage;

public class VkBroadcastMessage implements BroadcastMessage {

    private String text;

    public VkBroadcastMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFullText() {
        return getText();
    }

}
