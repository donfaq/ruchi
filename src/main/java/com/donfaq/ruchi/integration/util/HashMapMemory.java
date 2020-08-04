package com.donfaq.ruchi.integration.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class HashMapMemory {

    private LinkedHashMap<Integer, Object> memory;
    private static final int DEFAULT_SIZE = 5;
    private int counter = 0;

    public HashMapMemory() {
        initMemory(DEFAULT_SIZE);
    }

    public HashMapMemory(int size) {
        initMemory(size);
    }

    private void initMemory(int size) {
        this.memory = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Object> eldest) {
                return this.size() > size;
            }
        };
    }

    private String getHash(Object obj) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(obj.toString().getBytes());
            result = new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("Wrong hashing method specified", e);
        }
        return result;
    }


    public void add(Object obj) {
        memory.put(counter, getHash(obj));
        counter++;
    }

    public boolean contains(Object obj) {
        return memory.containsValue(getHash(obj));
    }
}
