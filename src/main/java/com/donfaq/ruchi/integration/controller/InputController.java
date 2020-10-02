package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.integration.model.vk.VkInputType;
import com.donfaq.ruchi.integration.service.input.TwitchInputService;
import com.donfaq.ruchi.integration.service.input.VkInputService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public Object vkCallback(@RequestBody String body) throws JsonProcessingException {
        log.info("New vk callback received: {}", body);
        ObjectMapper mapper = new ObjectMapper();
        return this.vkInputService.process(mapper.readValue(body, VkInputType.class));
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
