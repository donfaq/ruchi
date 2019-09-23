package com.donfaq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String VK_GROUP_CONFIRMATION_CODE = System.getenv("VK_GROUP_CONFIRMATION_CODE");

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/health")
    void health() {

    }

    @PostMapping("/callback")
    String callback(@RequestBody CallbackData callbackData) {
        String response = "ok";

        if (callbackData.getType().equals("confirmation")) {
            log.info("Confirmation request");
            response = VK_GROUP_CONFIRMATION_CODE;
        } else {
            log.info(callbackData.toString());
        }
        return response;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}