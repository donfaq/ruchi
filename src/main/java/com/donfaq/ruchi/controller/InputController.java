package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.service.input.twitch.TwitchWebSubHandler;
import com.donfaq.ruchi.service.input.vk.VkCallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InputController {
    private final VkCallbackHandler vkCallbackHandler;
    private final TwitchWebSubHandler twitchWebSubHandler;

    @PostMapping("/vk")
    public String vkCallback(@RequestBody String callbackMessage) {
        return vkCallbackHandler.parse(callbackMessage);
    }

    @GetMapping("/twitch")
    public ResponseEntity<String> twitchCallback(WebSubSubscriptionResponse response) {
        return this.twitchWebSubHandler.handleWebSubSubscriptionResponse(response);
    }

    @PostMapping("/twitch")
    public ResponseEntity<String> twitchCallback(@RequestBody String payload) {
        return this.twitchWebSubHandler.handleWebSubMessage(payload);
    }
}
