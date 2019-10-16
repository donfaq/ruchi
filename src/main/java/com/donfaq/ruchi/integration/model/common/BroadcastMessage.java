package com.donfaq.ruchi.integration.model.common;

public class BroadcastMessage {

    private String text;

    public BroadcastMessage() {
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
