package com.donfaq;

import com.donfaq.ruchi.integration.Application;
import com.donfaq.ruchi.integration.model.vk.Callback;
import com.donfaq.ruchi.integration.model.vk.Wallpost;
import com.donfaq.ruchi.integration.service.DiscordService;
import com.donfaq.ruchi.integration.service.VkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TestCallbackController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    DiscordService discordService;

    @MockBean
    VkService vkService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    JacksonTester<Callback> json;

    @Test
    public void testVerificationCallback() {
        try {

            final String TEST_TOKEN = "TEST_TOKEN";

            Callback inputCallback = new Callback();
            inputCallback.setType("confirmation");

            given(vkService.getConfirmationCode()).willReturn(TEST_TOKEN);
            given(vkService.isConfirmation(any())).willReturn(Boolean.TRUE);

            RequestBuilder requestBuilder = post("/callback")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.json.write(inputCallback).getJson());

            this.mvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().string(TEST_TOKEN));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWallpostCallback() throws Exception {
        Callback inputCallback = new Callback();
        inputCallback.setType("wallpost");
        Wallpost inputWallpost = new Wallpost();
        inputWallpost.setText("test");
        inputCallback.setObject(inputWallpost);

        RequestBuilder requestBuilder = post("/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.json.write(inputCallback).getJson());

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        then(vkService).should(times(1)).getText(any());
        then(discordService).should(times(1)).sendMessageToTextChannel(null);
    }


}
