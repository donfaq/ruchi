package com.donfaq.ruchi.service.input.vk;

import com.vk.api.sdk.objects.wall.Wallpost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

public class VkCallbackHandlerTest {
    private VkCallbackHandler vkCallbackHandler;
    private String confirmationCode;
    private VkWallpostProcessor vkWallpostProcessor;

    @BeforeEach
    void setUp() {
        confirmationCode = "testValue";
        vkWallpostProcessor = Mockito.mock(VkWallpostProcessor.class);
        vkCallbackHandler = new VkCallbackHandler(confirmationCode, vkWallpostProcessor);
    }

    @Test
    public void testConfirmationMessage() {
        String inputJson = "{\"type\": confirmation}";
        String result = vkCallbackHandler.parse(inputJson);
        assertEquals(confirmationCode, result);
    }

    @Test
    public void testWallpost() {
        String inputJson = "{\"type\": wall_post_new, \"object\": {}}";

        String result = vkCallbackHandler.parse(inputJson);
        assertEquals("ok", result);

        Mockito.verify(vkWallpostProcessor, Mockito.times(1))
               .process(eq(new Wallpost()));
    }

    @Test
    public void testNotWallpost() {
        String inputJson = "{\"type\": message, \"object\": {}}";

        String result = vkCallbackHandler.parse(inputJson);
        assertEquals("ok", result);

        Mockito.verify(vkWallpostProcessor, Mockito.times(0))
               .process(eq(new Wallpost()));
    }

}
