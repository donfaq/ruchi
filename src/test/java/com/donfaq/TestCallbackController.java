package com.donfaq;

import com.donfaq.ruchi.integration.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TestCallbackController {

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    DiscordService discordService;
//
//    @MockBean
//    VkService vkService;
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    JacksonTester<Callback> json;
//
//    @Test
//    public void testVerificationCallback() {
//        try {
//
//            final String TEST_TOKEN = "TEST_TOKEN";
//
//            Callback inputCallback = new Callback();
//            inputCallback.setType("confirmation");
//
//            given(vkService.getConfirmationCode()).willReturn(TEST_TOKEN);
//            given(vkService.isConfirmation(any())).willReturn(Boolean.TRUE);
//
//            RequestBuilder requestBuilder = post("/callback")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(this.json.write(inputCallback).getJson());
//
//            this.mvc.perform(requestBuilder)
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(TEST_TOKEN));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testWallpostCallback() throws Exception {
//        Callback inputCallback = new Callback();
//        inputCallback.setType("wallpost");
//        Wallpost inputWallpost = new Wallpost();
//        inputWallpost.setText("test");
//        inputCallback.setObject(inputWallpost);
//
//        RequestBuilder requestBuilder = post("/callback")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.json.write(inputCallback).getJson());
//
//        this.mvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().string("ok"));
//
//        then(vkService).should(times(1)).getText(any());
//        then(discordService).should(times(1)).sendMessageToTextChannel(null);
//    }


}
