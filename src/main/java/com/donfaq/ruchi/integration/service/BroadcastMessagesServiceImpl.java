package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.vk.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BroadcastMessagesServiceImpl implements BroadcastMessagesService {
    private final Logger log = LoggerFactory.getLogger(BroadcastMessagesServiceImpl.class);

    private DiscordService discordService;
    private VkService vkService;

    @Autowired
    public BroadcastMessagesServiceImpl(DiscordService discordService, VkService vkService) {
        this.discordService = discordService;
        this.vkService = vkService;
    }

    @Override
    public String spreadMessage(Callback callback) {
        String result = "ok";
        log.info("Received new callback message");
        if (vkService.isConfirmation(callback)) {
            log.info("Confirmation request");
            result = vkService.getConfirmationCode();
        } else {
            log.info("Broadcasting message");
            discordService.sendMessageToTextChannel(vkService.getText(callback));
        }
        return result;
    }
}
