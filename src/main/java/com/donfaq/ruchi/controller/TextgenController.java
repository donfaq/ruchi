package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.service.TextgenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TextgenController {
    private final TextgenService textgenService;

    @GetMapping("/textgen")
    public String getText(String model) {
        return textgenService.generate(model);
    }
}
