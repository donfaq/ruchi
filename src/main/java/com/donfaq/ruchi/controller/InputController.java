package com.donfaq.ruchi.controller;

import com.donfaq.ruchi.service.input.vk.VkCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InputController {
    private final VkCallbackHandler vkCallbackHandler;

    @Autowired
    public InputController(VkCallbackHandler vkCallbackHandler) {
        this.vkCallbackHandler = vkCallbackHandler;
    }

    @PostMapping("/vk")
    public String vkCallback(@RequestBody String callbackMessage) {
        return vkCallbackHandler.parse(callbackMessage);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
