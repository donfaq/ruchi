package com.donfaq.ruchi.integration.controller;

import com.donfaq.ruchi.integration.model.vk.Callback;
import com.donfaq.ruchi.integration.service.BroadcastMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {
    private BroadcastMessagesService broadcastMessagesService;

    @Autowired
    public CallbackController(BroadcastMessagesService broadcastMessagesService) {
        this.broadcastMessagesService = broadcastMessagesService;
    }

    @PostMapping("/callback")
    public String callback(@RequestBody Callback callback) {
        return broadcastMessagesService.spreadMessage(callback);
    }
}
