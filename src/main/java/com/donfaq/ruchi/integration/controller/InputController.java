package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.vk.Callback;
import com.donfaq.ruchi.integration.service.VkInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InputController {
    private VkInputService vkInputService;

    @Autowired
    public InputController(VkInputService vkInputService) {
        this.vkInputService = vkInputService;
    }

    @PostMapping("/vk")
    public Object vkCallback(@RequestBody Callback callback) {
        return this.vkInputService.process(callback);
    }
}
