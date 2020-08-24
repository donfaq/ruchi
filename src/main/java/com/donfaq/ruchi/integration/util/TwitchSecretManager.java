package com.donfaq.ruchi.integration.util;

import com.donfaq.ruchi.integration.model.twitch.api.TwitchResponse;
import com.donfaq.ruchi.integration.model.twitch.api.TwitchStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TwitchSecretManager {
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private String secret;

    @SneakyThrows
    private String createSignature(String secret, byte[] payload) {
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        return Hex.encodeHexString(mac.doFinal(payload));
    }

    public synchronized String getSecret() {
        if (secret == null) {
            secret = String.valueOf(UUID.randomUUID());
        }
        return secret;
    }

    @SneakyThrows
    public boolean validateSignature(String body, String signature) {
        return createSignature(this.secret, body.getBytes()).equals(signature);
    }
}
