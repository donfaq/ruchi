package com.donfaq;

import com.donfaq.ruchi.integration.Application;
import com.donfaq.ruchi.integration.service.DiscordService;
import com.donfaq.ruchi.integration.service.VkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TestCallbackController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    DiscordService discordService;

    @MockBean
    VkService vkService;

    @Test
    public void testVerificationCallback() {
        try {

            final String TEST_TOKEN = "TEST_TOKEN";

            when(vkService.getConfirmationCode()).thenReturn(TEST_TOKEN);
            when(vkService.isConfirmation(any())).thenReturn(Boolean.TRUE);

            RequestBuilder requestBuilder = post("/callback")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"type\": \"confirmation\"}");

            this.mvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().string(TEST_TOKEN));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
