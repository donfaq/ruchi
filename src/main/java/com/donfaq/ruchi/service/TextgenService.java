package com.donfaq.ruchi.service;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextgenService {
    private final TextGeneratorCommand textGen;

    public String generate(String model) {
        String defaultResponse = "Что-то пошло не так";
        String responseMessage = defaultResponse;

        try {
            responseMessage = switch (model) {
                case "speak" -> textGen.speak("").orElse(defaultResponse);
                case "pron" -> textGen.pron("").orElse(defaultResponse);
                case "gachi" -> textGen.gachi("").orElse(defaultResponse);
                case "kalik" -> textGen.kalik("").orElse(defaultResponse);
                case "woman" -> textGen.woman("").orElse(defaultResponse);
                case "spam" -> textGen.spam("").orElse(defaultResponse);
                case "vata" -> textGen.vata("").orElse(defaultResponse);
                default -> defaultResponse;
            };
        } catch (Exception e) {
            log.warn("Exception during response generation", e);
        }
        return responseMessage;
    }
}
