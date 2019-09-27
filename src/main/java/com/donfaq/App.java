package com.donfaq;

import com.donfaq.objects.CallbackData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final DiscordProcessor discord = new DiscordProcessor();
    private static final String VK_GROUP_CONFIRMATION_CODE = System.getenv("VK_GROUP_CONFIRMATION_CODE");

    @PostMapping("/callback")
    public String callback(@RequestBody CallbackData callbackData) {
        String response = "ok";

        if (callbackData.getType().equals("confirmation")) {
            log.info("Confirmation request");
            response = VK_GROUP_CONFIRMATION_CODE;
        } else {
            log.info(callbackData.toString());
            discord.processData(callbackData);
        }

        return response;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}