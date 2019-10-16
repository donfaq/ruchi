package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.common.BroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BroadcastServiceImpl implements BroadcastService {
    private final Logger log = LoggerFactory.getLogger(BroadcastServiceImpl.class);
    private List<OutputService> outputServices;
    private MessagesMemory memory;

    @Autowired
    public BroadcastServiceImpl(List<OutputService> outputServices, MessagesMemory memory) {
        this.outputServices = outputServices;
        this.memory = memory;
    }

    @Override
    public void broadcast(BroadcastMessage message) {
        if(!this.memory.contains(message)){
            log.info("Broadcasting new message");
            this.memory.add(message);
            outputServices.parallelStream().forEach(outputService -> outputService.send(message));
        }
    }
}
