package com.donfaq.ruchi.integration.model.vk;

import com.donfaq.ruchi.integration.model.BroadcastMessage;

import java.net.URL;
import java.util.List;

public class VkBroadcastMessage implements BroadcastMessage {
    private String text;
    private List<URL> images;

    public VkBroadcastMessage() {
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public List<URL> getImages() {
        return images;
    }

    public void setImages(List<URL> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "VkBroadcastMessage{" +
                "text='" + text + '\'' +
                ", images=" + images +
                '}';
    }
}
