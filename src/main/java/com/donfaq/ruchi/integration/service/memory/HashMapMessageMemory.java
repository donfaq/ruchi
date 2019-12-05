package com.donfaq.ruchi.integration.service.memory;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class HashMapMessageMemory implements MessagesMemory {

    private LinkedHashMap<Integer, String> memory;
    private static final int DEFAULT_SIZE = 5;
    private static final Logger log = LoggerFactory.getLogger(HashMapMessageMemory.class);
    private int counter = 0;

    public HashMapMessageMemory() {
        initMemory(DEFAULT_SIZE);
    }

    public HashMapMessageMemory(int size) {
        initMemory(size);
    }

    private void initMemory(int size) {
        this.memory = new LinkedHashMap<Integer, String>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return this.size() > size;
            }
        };
    }

    private String getMessageHash(BroadcastMessage message) {
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
        memory.put(counter, getMessageHash(message));
        counter++;
    }

    @Override
    public boolean contains(BroadcastMessage message) {
        return memory.containsValue(getMessageHash(message));
    }
}
