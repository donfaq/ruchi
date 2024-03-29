package com.donfaq.ruchi.component.commands;

import com.donfaq.ruchi.component.models.textgen.TextGeneratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TextGeneratorCommand {
    private final TextGeneratorClient textGeneratorClient;

    @Autowired
    public TextGeneratorCommand(TextGeneratorClient textGeneratorClient) {
        this.textGeneratorClient = textGeneratorClient;
    }

    private static final int UNLIMITED_LENGTH = -1;

    private Optional<String> generateText(String model, String query, int maxLenght) {
        return Optional.of(textGeneratorClient.generateText(model, query, maxLenght));
    }

    public Optional<String> speak(String query, int maxLength) {
        return generateText("chat", query, maxLength);
    }

    public Optional<String> speak(String query) {
        return generateText("chat", query, UNLIMITED_LENGTH);
    }

    public Optional<String> pron(String query, int maxLength) {
        return generateText("pron", query, maxLength);
    }

    public Optional<String> pron(String query) {
        return generateText("pron", query, UNLIMITED_LENGTH);
    }

    public Optional<String> gachi(String query, int maxLength) {
        return generateText("gachi", query, maxLength);
    }

    public Optional<String> gachi(String query) {
        return generateText("gachi", query, UNLIMITED_LENGTH);
    }

    public Optional<String> kalik(String query, int maxLength) {
        return generateText("kalik", query, maxLength);
    }

    public Optional<String> kalik(String query) {
        return generateText("kalik", query, UNLIMITED_LENGTH);
    }

    public Optional<String> woman(String query) {return generateText("woman", query, UNLIMITED_LENGTH);}

    public Optional<String> woman(String query, int maxLength) {
        return generateText("woman", query, maxLength);
    }

    public Optional<String> spam(String query) {return generateText("spam", query, UNLIMITED_LENGTH);}

    public Optional<String> spam(String query, int maxLength) {
        return generateText("spam", query, maxLength);
    }

    public Optional<String> vata(String query) {return generateText("patriot", query, UNLIMITED_LENGTH);}

    public Optional<String> vata(String query, int maxLength) {
        return generateText("patriot", query, maxLength);
    }

}
