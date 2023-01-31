package com.donfaq.ruchi.util;

import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.model.BroadcastMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockingMemoryTest {
    @Test
    public void testAddMessage() {
        BlockingMemory memory = new BlockingMemory();

        BroadcastMessage message = new BroadcastMessage("hello", null);

        assertFalse(memory.contains(message));
        memory.add(message);
        assertTrue(memory.contains(message));
        memory.add(message);
        assertTrue(memory.contains(message));
    }

    @Test
    public void testMemorySize() {
        BlockingMemory memory = new BlockingMemory(2);

        BroadcastMessage message1 = new BroadcastMessage("message1", null);
        BroadcastMessage message2 = new BroadcastMessage("message2", null);
        BroadcastMessage message3 = new BroadcastMessage("message3", null);

        memory.add(message1);
        memory.add(message2);
        assertTrue(memory.contains(message1));
        memory.add(message3);
        assertFalse(memory.contains(message1));
    }

}
