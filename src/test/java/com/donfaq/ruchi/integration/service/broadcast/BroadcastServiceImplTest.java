package com.donfaq.ruchi.integration.service.broadcast;

import com.donfaq.ruchi.integration.model.BroadcastMessage;
import com.donfaq.ruchi.integration.service.input.TwitchInputService;
import com.donfaq.ruchi.integration.service.input.VkInputService;
import com.donfaq.ruchi.integration.service.memory.MessagesMemory;
import com.donfaq.ruchi.integration.service.output.DiscordOutputService;
import com.donfaq.ruchi.integration.service.output.TelegramOutputService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BroadcastServiceImplTest {

    @MockBean
    private DiscordOutputService discordOutputService;

    @MockBean
    private TelegramOutputService telegramOutputService;

    @MockBean
    private MessagesMemory memory;

    @MockBean
    private TwitchInputService twitchInputService;

    @MockBean
    private VkInputService vkInputService;

    @Autowired
    private BroadcastServiceImpl broadcastService;


    @Test
    public void broadcast() {
        BroadcastMessage message = new BroadcastMessage();

        when(memory.contains(message)).thenReturn(false);
        broadcastService.broadcast(message, false);
        Mockito.verify(discordOutputService, times(1)).send(ArgumentMatchers.eq(message));
        Mockito.verify(telegramOutputService, times(1)).send(ArgumentMatchers.eq(message));

        when(memory.contains(message)).thenReturn(true);
        broadcastService.broadcast(message, false);
        Mockito.verifyNoMoreInteractions(discordOutputService);
        Mockito.verifyNoMoreInteractions(telegramOutputService);
    }
}
