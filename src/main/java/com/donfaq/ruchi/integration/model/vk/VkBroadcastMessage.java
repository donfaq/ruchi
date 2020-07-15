package com.donfaq.ruchi.integration.model.vk;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class VkBroadcastMessage implements BroadcastMessage {
    private String text;
    private List<URL> images;
}
