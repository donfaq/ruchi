package com.donfaq.ruchi.component.models.textgen;

import com.donfaq.ruchi.config.properties.ModelConfigProperties;
import com.donfaq.ruchi.proto.TextGenerationRequest;
import com.donfaq.ruchi.proto.TextGenerationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TextGeneratorClient {
    private final TextGenerationServiceGrpc.TextGenerationServiceBlockingStub blockingStub;

    private final Logger log = LoggerFactory.getLogger(TextGeneratorClient.class);

    public TextGeneratorClient(ModelConfigProperties properties) {
        log.info("Initializing RPC connection to text generator");
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(properties.address(), properties.port())
                .usePlaintext()
                .build();
        blockingStub = TextGenerationServiceGrpc.newBlockingStub(managedChannel);
    }

    public String generateText(String model, String query, Integer maxLength) {
        log.info(String.format("Generating text with predicate '%s'", query));
        String result;
        if (Objects.isNull(query)) {
            query = "";
        }
        TextGenerationRequest request = TextGenerationRequest
                .newBuilder()
                .setModelName(model)
                .setQuery(query)
                .setMaxLength(maxLength)
                .build();
        try {
            result = blockingStub.generate(request).getMessage();
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed: {}", e.getStatus());
            return null;
        }
        return result;
    }

}
