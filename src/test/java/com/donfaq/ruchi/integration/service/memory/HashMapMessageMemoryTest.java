package com.donfaq.ruchi.integration.service.memory;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashMapMessageMemoryTest {
    @Test
    public void testAddMessage() {
        MessagesMemory memory = new HashMapMessageMemory();

        BroadcastMessage message = new BroadcastMessage();
        message.setText("hello");

        assertFalse(memory.contains(message));
        memory.add(message);
        assertTrue(memory.contains(message));
        memory.add(message);
        assertTrue(memory.contains(message));
    }

    @Test
    public void testMemorySize() {
        MessagesMemory memory = new HashMapMessageMemory(2);

        BroadcastMessage message1 = new BroadcastMessage();
        message1.setText("message1");
        BroadcastMessage message2 = new BroadcastMessage();
        message2.setText("message2");
        BroadcastMessage message3 = new BroadcastMessage();
        message3.setText("message3");

        memory.add(message1);
        memory.add(message2);
        assertTrue(memory.contains(message1));
        memory.add(message3);
        assertFalse(memory.contains(message1));
    }

}
