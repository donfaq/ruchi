package com.donfaq.ruchi.service;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.output.OutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BroadcastService {
    private final List<OutputService> outputServices;

    private final Logger log = LoggerFactory.getLogger(BroadcastService.class);

    @Autowired
    public BroadcastService(List<OutputService> outputServices) {
        this.outputServices = outputServices;
    }

    public void broadcast(BroadcastMessage message) {
        log.info("Broadcasting new message");
        outputServices.parallelStream().forEach(outputService -> outputService.send(message));
    }
}
