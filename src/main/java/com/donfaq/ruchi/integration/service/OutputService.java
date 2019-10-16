package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.common.BroadcastMessage;

public interface OutputService {
    void send(BroadcastMessage broadcastMessage);
}
