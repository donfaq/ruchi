package com.donfaq.ruchi.service.broadcast;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.input.TwitchInputService;
import com.donfaq.ruchi.service.input.VkInputService;
import com.donfaq.ruchi.service.output.DiscordOutputService;
import com.donfaq.ruchi.service.output.TelegramOutputService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
public class BroadcastServiceImplTest {

    @MockBean
    private DiscordOutputService discordOutputService;

    @MockBean
    private TelegramOutputService telegramOutputService;

    @MockBean
    private TwitchInputService twitchInputService;

    @MockBean
    private VkInputService vkInputService;

    @Autowired
    private BroadcastServiceImpl broadcastService;

    @Test
    public void broadcast() {
        BroadcastMessage message = new BroadcastMessage();
        broadcastService.broadcast(message);
        Mockito.verify(discordOutputService,
                times(1)).send(ArgumentMatchers.eq(message));
        Mockito.verify(telegramOutputService,
                times(1)).send(ArgumentMatchers.eq(message));

    }
}