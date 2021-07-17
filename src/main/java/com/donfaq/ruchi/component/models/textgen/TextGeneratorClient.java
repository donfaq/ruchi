package com.donfaq.ruchi.component.models.textgen;

import com.donfaq.ruchi.config.properties.ModelConfigProperties;
import com.donfaq.ruchi.proto.TextGenerationRequest;
import com.donfaq.ruchi.proto.TextGenerationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class TextGeneratorClient {
    private final TextGenerationServiceGrpc.TextGenerationServiceBlockingStub blockingStub;

    public TextGeneratorClient(ModelConfigProperties properties) {
        log.info("Initializing RPC connection to text generator");
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(properties.getAddress(), properties.getPort())
                .usePlaintext()
                .build();
        blockingStub = TextGenerationServiceGrpc.newBlockingStub(managedChannel);
    }

    public String generateText(String model, String query, int maxLength) {
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
