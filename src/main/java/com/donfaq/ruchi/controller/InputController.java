package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.service.input.vk.VkCallbackHandler;
import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.service.input.TwitchInputService;
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
    private final TwitchInputService twitchInputService;

    @PostMapping("/vk")
    public String vkCallback(@RequestBody String callbackMessage) {
        return vkCallbackHandler.parse(callbackMessage);
    }

    @GetMapping("/twitch")
    public String twitchCallback(@RequestParam WebSubSubscriptionResponse response) {
        return this.twitchInputService.processWebhookSubscriptionResponse(response);
    }

    @PostMapping("/twitch")
    public ResponseEntity<String> twitchCallback(@RequestBody String payload,
                                                 @RequestHeader(value = "X-Hub-Signature") String signature) {
        return this.twitchInputService.processWebhookNotification(signature, payload);
    }
}
