package com.donfaq.ruchi.service.input.vk;

import com.donfaq.ruchi.config.properties.VkConfigProperties;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vk.api.sdk.events.Events;
import com.vk.api.sdk.objects.callback.messages.CallbackMessage;
import com.vk.api.sdk.objects.wall.Wallpost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VkCallbackHandler {
    private final VkWallpostProcessor wallpostProcessor;
    private final VkConfigProperties vkConfig;

    public VkCallbackHandler(VkConfigProperties vkConfig, VkWallpostProcessor wallpostProcessor) {
        this.vkConfig = vkConfig;
        this.wallpostProcessor = wallpostProcessor;
    }

    private final Gson gson = new Gson();
    private static final String OK_RESPONSE = "ok";

    private void wallPostNew(Wallpost wallpost) {
        wallpostProcessor.process(wallpost);
    }

    public String parse(String json) {
        return parse(gson.fromJson(json, CallbackMessage.class));
    }

    private <T> T designateObject(JsonObject object, Events type) {
        return gson.fromJson(object, type.getType());
    }

    public String parse(CallbackMessage message) {
        switch (message.getType()) {
            case CONFIRMATION:
                return confirmation();
            case WALL_POST_NEW:
                wallPostNew(designateObject(message.getObject(), message.getType()));
                break;
            default:
                log.error("Unexpected callback event type received");
                return null;
        }
        return OK_RESPONSE;
    }

    private String confirmation() {
        return this.vkConfig.getConfirmationCode();
    }

}
