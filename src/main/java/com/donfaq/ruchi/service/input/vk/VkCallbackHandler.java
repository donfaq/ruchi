package com.donfaq.ruchi.service.input.vk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.objects.callback.messages.CallbackMessage;
import com.vk.api.sdk.objects.wall.Wallpost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

@Slf4j
@Component
public class VkCallbackHandler {
    private final String confirmationCode;
    private final VkWallpostProcessor wallpostProcessor;

    public VkCallbackHandler(
            @Value("${vk.confirmationCode}") String confirmationCode,
            VkWallpostProcessor wallpostProcessor
    ) {
        this.confirmationCode = confirmationCode;
        this.wallpostProcessor = wallpostProcessor;
    }

    private final Gson gson = new Gson();
    private final static Map<String, Type> CALLBACK_TYPES;
    private static final String CALLBACK_EVENT_WALL_POST_NEW = "wall_post_new";
    private static final String CALLBACK_EVENT_CONFIRMATION = "confirmation";
    private static final String OK_RESPONSE = "ok";

    static {
        CALLBACK_TYPES = Map.of(CALLBACK_EVENT_WALL_POST_NEW, new TypeToken<CallbackMessage<Wallpost>>() {}.getType());
    }

    private void wallPostNew(Wallpost wallpost) {
        log.info("New wallpost: {}", wallpost.toPrettyString());
        wallpostProcessor.process(wallpost);
    }

    public String parse(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return parse(jsonObject);
    }

    public String parse(JsonObject json) {
        String type = json.get("type").getAsString();
        if (type.equalsIgnoreCase(CALLBACK_EVENT_CONFIRMATION)) {
            return confirmationCode;
        }
        Type typeOfClass = CALLBACK_TYPES.get(type);
        if (typeOfClass == null) {
            log.warn("Unsupported callback event: {}", type);
        }

        if (CALLBACK_EVENT_WALL_POST_NEW.equals(type)) {
            CallbackMessage<Wallpost> message = gson.fromJson(json, typeOfClass);
            wallPostNew(message.getObject());
        }

        return OK_RESPONSE;
    }


}
