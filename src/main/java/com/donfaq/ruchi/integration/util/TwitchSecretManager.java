package com.donfaq.ruchi.integration.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitchSecretManager {
    private String secret;


    public synchronized String getSecret() {
        if (secret == null) {
            secret = String.valueOf(UUID.randomUUID());
        }
        return secret;
    }

    @SneakyThrows
    public boolean validateSignature(String payload, String signature) {
        String computed = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmacHex(payload);
        log.info("Validating signature. Computed='{}'. Received='{}'", computed, signature);
        return MessageDigest.isEqual(signature.getBytes(), computed.getBytes());
    }
}
