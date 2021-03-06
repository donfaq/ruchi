package com.donfaq.ruchi.service;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.output.OutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BroadcastService {
    private final List<OutputService> outputServices;

    public void broadcast(BroadcastMessage message) {
        log.info("Broadcasting new message");
        outputServices.parallelStream().forEach(outputService -> outputService.send(message));
    }
}
