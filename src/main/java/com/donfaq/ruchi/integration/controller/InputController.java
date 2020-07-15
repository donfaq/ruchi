package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.vk.VkInputType;
import com.donfaq.ruchi.integration.service.input.VkInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InputController {
    private final VkInputService vkInputService;

    @Autowired
    public InputController(VkInputService vkInputService) {
        this.vkInputService = vkInputService;
    }

    @PostMapping("/vk")
    public Object vkCallback(@RequestBody VkInputType callback) {
        return this.vkInputService.process(callback);
    }
}
