package com.donfaq.ruchi.integration.service.output;

import com.donfaq.ruchi.integration.model.BroadcastMessage;

public interface OutputService {
    void send(BroadcastMessage message);
}
