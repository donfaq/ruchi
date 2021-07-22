package com.donfaq.ruchi.component.commands;

import com.donfaq.ruchi.component.models.textgen.TextGeneratorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextGeneratorCommand {
    private final TextGeneratorClient textGeneratorClient;
    private static final Integer DEFAULT_MAX_LENGTH = 240;

    private Optional<String> generateText(String model, String query, Integer maxLenght) {
        return Optional.of(textGeneratorClient.generateText(model, query, maxLenght));
    }

    public Optional<String> speak(String query, Integer maxLength) {
        return generateText("chat", query, maxLength);
    }

    public Optional<String> speak(String query) {
        return generateText("chat", query, DEFAULT_MAX_LENGTH);
    }

    public Optional<String> pron(String query, Integer maxLength) {
        return generateText("pron", query, maxLength);
    }

    public Optional<String> pron(String query) {
        return generateText("pron", query, DEFAULT_MAX_LENGTH);
    }

    public Optional<String> gachi(String query, Integer maxLength) {
        return generateText("gachi", query, maxLength);
    }

    public Optional<String> gachi(String query) {
        return generateText("gachi", query, DEFAULT_MAX_LENGTH);
    }

    public Optional<String> kalik(String query, Integer maxLength) {
        return generateText("kalik", query, maxLength);
    }

    public Optional<String> kalik(String query) {
        return generateText("kalik", query, DEFAULT_MAX_LENGTH);
    }

}
