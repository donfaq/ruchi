package com.donfaq.ruchi.integration.service.memory;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class DequeMessageMemory implements MessagesMemory {
    private final Logger log = LoggerFactory.getLogger(DequeMessageMemory.class);
    private Deque<String> memory;

    public DequeMessageMemory() {
        this.memory = new ArrayDeque<>(10);
    }

    private String getId(BroadcastMessage message) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.toString().getBytes());
            result = new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("Wrong hashing method specified", e);
        }
        return result;
    }


    @Override
    public void add(BroadcastMessage message) {
        this.memory.add(getId(message));
    }

    @Override
    public boolean contains(BroadcastMessage message) {
        return this.memory.contains(getId(message));
    }
}
