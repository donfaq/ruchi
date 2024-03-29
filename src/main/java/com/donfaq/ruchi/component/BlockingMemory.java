package com.donfaq.ruchi.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class BlockingMemory {

    private final Logger log = LoggerFactory.getLogger(BlockingMemory.class);
    private final ArrayBlockingQueue<String> memory;
    private static final int DEFAULT_SIZE = 5;

    public BlockingMemory() {
        memory = new ArrayBlockingQueue<>(DEFAULT_SIZE);
    }

    public BlockingMemory(int size) {
        memory = new ArrayBlockingQueue<>(size);
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


    public synchronized void add(Object obj) {
        if (memory.remainingCapacity() == 0) {
            memory.poll();
        }
        memory.add(getHash(obj));
    }

    public synchronized boolean contains(Object obj) {
        return memory.contains(getHash(obj));
    }
}
