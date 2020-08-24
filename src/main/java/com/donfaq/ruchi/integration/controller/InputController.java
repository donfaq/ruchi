package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.twitch.api.TwitchResponse;
import com.donfaq.ruchi.integration.model.twitch.api.TwitchStream;
import com.donfaq.ruchi.integration.model.twitch.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.integration.model.vk.VkInputType;
import com.donfaq.ruchi.integration.service.input.TwitchInputService;
import com.donfaq.ruchi.integration.service.input.VkInputService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InputController {
    private final VkInputService vkInputService;
    private final ObjectMapper objectMapper;
    private final TwitchInputService twitchInputService;

    @PostMapping("/vk")
    public Object vkCallback(@RequestBody VkInputType callback) {
        return this.vkInputService.process(callback);
    }

    @GetMapping("/twitch")
    public String twitchCallback(@RequestParam Map<String, String> requestParams) {
        ObjectMapper mapper = new ObjectMapper();
        WebSubSubscriptionResponse response = mapper.convertValue(requestParams, WebSubSubscriptionResponse.class);
        return this.twitchInputService.processWebhookSubscriptionResponse(response);
    }

    @PostMapping("/twitch")
    public void twitchCallback(
            @RequestBody HttpEntity<String> payload,
            @RequestHeader(value = "X-Hub-Signature") String signature
    ) throws JsonProcessingException {
        TwitchResponse<TwitchStream> body = objectMapper.readValue(
                payload.getBody(),
                new TypeReference<TwitchResponse<TwitchStream>>() {});
        this.twitchInputService.processWebhookNotification(body, signature, payload.getBody());
    }
}
