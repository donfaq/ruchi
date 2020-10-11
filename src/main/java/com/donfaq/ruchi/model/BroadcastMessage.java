package com.donfaq.ruchi.model;

import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class BroadcastMessage {
    private String text;
    private List<URL> images;

}
