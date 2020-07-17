package com.donfaq.ruchi.integration.service.memory;

import com.donfaq.ruchi.integration.model.vk.VkBroadcastMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashMapMessageMemoryTest {
    @Test
    public void testAddMessage() {
        MessagesMemory memory = new HashMapMessageMemory();

        VkBroadcastMessage message = new VkBroadcastMessage();
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

        VkBroadcastMessage message1 = new VkBroadcastMessage();
        message1.setText("message1");
        VkBroadcastMessage message2 = new VkBroadcastMessage();
        message2.setText("message2");
        VkBroadcastMessage message3 = new VkBroadcastMessage();
        message3.setText("message3");


        memory.add(message1);
        memory.add(message2);
        assertTrue(memory.contains(message1));
        memory.add(message3);
        assertFalse(memory.contains(message1));
    }

}
