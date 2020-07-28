package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.twitch.webhook.websub.WebSubSubscriptionResponse;
import com.donfaq.ruchi.integration.model.vk.VkInputType;
import com.donfaq.ruchi.integration.service.input.TwitchInputService;
import com.donfaq.ruchi.integration.service.input.VkInputService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InputController {
    private final VkInputService vkInputService;
    private final TwitchInputService twitchInputService;

    @PostMapping("/vk")
    public Object vkCallback(@RequestBody VkInputType callback) {
        return this.vkInputService.process(callback);
    }

    @PostMapping("/twitch")
    public Object twitchCallback(@RequestBody WebSubSubscriptionResponse response) {
        return this.twitchInputService.process(response);
    }
}
