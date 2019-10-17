package com.donfaq.ruchi.integration.service.memory;

import com.donfaq.ruchi.integration.model.BroadcastMessage;

public interface MessagesMemory {
    void add(BroadcastMessage message);

    boolean contains(BroadcastMessage message);
}
