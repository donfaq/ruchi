package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.service.input.vk.VkCallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InputController {
    private final VkCallbackHandler vkCallbackHandler;

    @PostMapping("/vk")
    public String vkCallback(@RequestBody String callbackMessage) {
        return vkCallbackHandler.parse(callbackMessage);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
