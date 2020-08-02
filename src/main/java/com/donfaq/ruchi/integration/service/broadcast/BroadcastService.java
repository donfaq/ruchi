package com.donfaq.ruchi.integration.service.broadcast;


import com.donfaq.ruchi.integration.model.BroadcastMessage;

public interface BroadcastService {
    void broadcast(BroadcastMessage message, Boolean ignoreMemory);
}
