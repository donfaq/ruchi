package com.donfaq.ruchi.model;

import java.net.URL;
import java.util.List;


public record BroadcastMessage(
        String text,
        List<URL> images
) {
}
