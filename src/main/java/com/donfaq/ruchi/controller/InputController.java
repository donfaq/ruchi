package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.service.input.twitch.TwitchWebSubHandler;
import com.donfaq.ruchi.service.input.vk.VkCallbackHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InputController {
    private final VkCallbackHandler vkCallbackHandler;
    private final TwitchWebSubHandler twitchWebSubHandler;
    private final ObjectMapper objectMapper;

    @PostMapping("/vk")
    public String vkCallback(@RequestBody String callbackMessage) {
        return vkCallbackHandler.parse(callbackMessage);
    }

    @GetMapping("/twitch")
    public ResponseEntity<String> twitchCallback(@RequestParam Map<String, String> requestParams) {
        return twitchWebSubHandler.handleWebSubSubscriptionResponse(
                objectMapper.convertValue(requestParams, WebSubSubscriptionResponse.class)
        );
    }

    @PostMapping("/twitch")
    public ResponseEntity<String> twitchCallback(@RequestBody String payload) {
        return this.twitchWebSubHandler.handleWebSubMessage(payload);
    }
}
