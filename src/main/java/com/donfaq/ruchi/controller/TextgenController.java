package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.service.TextgenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextgenController {
    private final TextgenService textgenService;

    @Autowired
    public TextgenController(TextgenService textgenService) {
        this.textgenService = textgenService;
    }

    @GetMapping("/textgen")
    public String getText(String model) {
        return textgenService.generate(model);
    }
}
