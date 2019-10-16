package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.common.BroadcastMessage;

public interface MessagesMemory {
    void add(BroadcastMessage message);

    boolean contains(BroadcastMessage message);
}
