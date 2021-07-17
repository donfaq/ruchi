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

    private Optional<String> generateText(String model, String query) {
        return Optional.of(textGeneratorClient.generateText(model, query, 240));
    }

    public Optional<String> speak(String query) {
        return generateText("chat", query);
    }

    public Optional<String> pron(String query) {
        return generateText("pron", query);
    }

    public Optional<String> gachi(String query) {
        return generateText("gachi", query);
    }

    public Optional<String> kalik(String query) {
        return generateText("kalik", query);
    }
}
