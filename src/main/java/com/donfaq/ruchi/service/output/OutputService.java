package com.donfaq.ruchi.service.output;

import com.donfaq.ruchi.model.BroadcastMessage;

public interface OutputService {
    void send(BroadcastMessage message);
}
