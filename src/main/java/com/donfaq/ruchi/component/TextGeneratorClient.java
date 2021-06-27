package com.donfaq.ruchi.component;

import com.donfaq.ruchi.proto.TextGenerationRequest;
import com.donfaq.ruchi.proto.TextGenerationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TextGeneratorClient {
    private TextGenerationServiceGrpc.TextGenerationServiceBlockingStub blockingStub;

    @PostConstruct
    private void init() {
        log.info("Initializing RPC connection to text generator");
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        blockingStub = TextGenerationServiceGrpc.newBlockingStub(managedChannel);
    }

    public String generateText(String query, int maxLength) {
        log.info(String.format("Generating text with predicate '%s'", query));
        String result;
        if (query == null) {
            query = "";
        }
        TextGenerationRequest request = TextGenerationRequest
                .newBuilder()
                .setModelName("chat")
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
