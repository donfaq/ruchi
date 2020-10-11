package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.model.vk.VkInputType;
import com.donfaq.ruchi.service.input.TwitchInputService;
import com.donfaq.ruchi.service.input.VkInputService;
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
    private final VkInputService vkInputService;

    private final TwitchInputService twitchInputService;

    @PostMapping("/vk")
    public String vkCallback(@RequestBody VkInputType callbackMessage) {
        return this.vkInputService.process(callbackMessage);
    }

    @GetMapping("/twitch")
    public String twitchCallback(@RequestParam Map<String, String> requestParams) {
        ObjectMapper mapper = new ObjectMapper();
        WebSubSubscriptionResponse response = mapper.convertValue(requestParams, WebSubSubscriptionResponse.class);
        return this.twitchInputService.processWebhookSubscriptionResponse(response);
    }

    @PostMapping("/twitch")
    public ResponseEntity<String> twitchCallback(
            @RequestBody String payload,
            @RequestHeader(value = "X-Hub-Signature") String signature
    ) {
        return this.twitchInputService.processWebhookNotification(signature, payload);
    }
}
