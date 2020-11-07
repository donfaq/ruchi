package com.donfaq.ruchi.service.input.vk;


import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.BroadcastService;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.PhotoSizesType;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

class VkWallpostProcessorTest {
    private VkWallpostProcessor wallpostProcessor;
    private BlockingMemory memory;
    private VkApiService vkApiService;
    private BroadcastService broadcastService;

    private final String TRIGGER = "triggerString";
    private final String NOT_TRIGGER = "some text";

    @BeforeEach
    void setUp() {
        memory = new BlockingMemory();
        vkApiService = Mockito.mock(VkApiService.class);
        broadcastService = Mockito.mock(BroadcastService.class);
        wallpostProcessor = new VkWallpostProcessor(TRIGGER, vkApiService, broadcastService, memory);
    }

    @Test
    public void testWallpostWithTrigger() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TRIGGER);
        wallpost.setPostType(PostType.POST);

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setText(TRIGGER);

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1))
               .broadcast(eq(broadcastMessage));
        Mockito.verifyNoInteractions(vkApiService);
    }

    @Test
    public void testWallpostWithoutTrigger() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(NOT_TRIGGER);
        wallpost.setPostType(PostType.POST);

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setText(NOT_TRIGGER);

        assertFalse(memory.contains(broadcastMessage));
        Mockito.verifyNoInteractions(broadcastService);
        Mockito.verifyNoInteractions(vkApiService);
    }

    @Test
    public void testWallpostWithTriggerAndPhotoAttachment() throws MalformedURLException {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TRIGGER);
        wallpost.setPostType(PostType.POST);

        WallpostAttachment photoAttachment = new WallpostAttachment();
        photoAttachment.setType(WallpostAttachmentType.PHOTO);
        Photo photo = new Photo();
        PhotoSizes photoSizeM = new PhotoSizes();
        photoSizeM.setType(PhotoSizesType.M);
        URL urlM = new URL("https://test.com/M");
        photoSizeM.setUrl(urlM);
        PhotoSizes photoSizeZ = new PhotoSizes();
        photoSizeZ.setType(PhotoSizesType.Z);
        URL urlZ = new URL("https://test.com/Z");
        photoSizeZ.setUrl(urlZ);
        photo.setSizes(List.of(photoSizeM, photoSizeZ));
        photoAttachment.setPhoto(photo);

        WallpostAttachment notPhotoAttachment = new WallpostAttachment();
        notPhotoAttachment.setType(WallpostAttachmentType.GRAFFITI);

        wallpost.setAttachments(List.of(photoAttachment, notPhotoAttachment));


        Mockito.when(vkApiService.updatePhotoInfo(List.of(photo))).thenReturn(List.of(photo));
        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setText(TRIGGER);
        broadcastMessage.setImages(List.of(urlZ));

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1))
               .broadcast(eq(broadcastMessage));
        Mockito.verify(vkApiService, Mockito.times(1)).updatePhotoInfo(List.of(photo));
    }

    @Test
    public void testWallpostWithTriggerWithoutPhotoAttachment() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TRIGGER);
        wallpost.setPostType(PostType.POST);

        WallpostAttachment notPhotoAttachment = new WallpostAttachment();
        notPhotoAttachment.setType(WallpostAttachmentType.GRAFFITI);

        wallpost.setAttachments(List.of(notPhotoAttachment));

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setText(TRIGGER);

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1))
               .broadcast(eq(broadcastMessage));
        Mockito.verifyNoInteractions(vkApiService);
    }
}
