package com.donfaq.ruchi.integration.service.broadcast;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.donfaq.ruchi.integration.service.memory.MessagesMemory;
import com.donfaq.ruchi.integration.service.output.OutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BroadcastServiceImpl implements BroadcastService {
    private final List<OutputService> outputServices;
    private final MessagesMemory memory;

    @Autowired
    public BroadcastServiceImpl(List<OutputService> outputServices, MessagesMemory memory) {
        this.outputServices = outputServices;
        this.memory = memory;
    }

    @Override
    public void broadcast(BroadcastMessage message, Boolean ignoreMemory) {
        if (this.memory.contains(message) & !ignoreMemory) {
            log.info("Received message that has already been processed");
            return;
        }
        log.info("Broadcasting new message");
        this.memory.add(message);
        outputServices.parallelStream().forEach(outputService -> outputService.send(message));
    }
}
