package com.donfaq.ruchi.integration.service.broadcast;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.donfaq.ruchi.integration.service.output.OutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BroadcastServiceImpl implements BroadcastService {
    private final List<OutputService> outputServices;

    @Override
    public void broadcast(BroadcastMessage message) {
        log.info("Broadcasting new message");
        outputServices.parallelStream().forEach(outputService -> outputService.send(message));
    }
}
