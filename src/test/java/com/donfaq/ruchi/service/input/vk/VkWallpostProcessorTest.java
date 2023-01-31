package com.donfaq.ruchi.service.input.vk;


import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.config.properties.VkConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.BroadcastService;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.PhotoSizesType;
import com.vk.api.sdk.objects.photos.responses.GetByIdLegacyResponse;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private VkConfigProperties vkConfig;

    private final String TEXT_WITH_TRIGGER = "triggerString";
    private final String TEXT_WITHOUT_TRIGGER = "some text";

    @BeforeEach
    void setUp() {
        memory = new BlockingMemory();
        vkApiService = Mockito.mock(VkApiService.class);
        broadcastService = Mockito.mock(BroadcastService.class);

        vkConfig = new VkConfigProperties("", "", TEXT_WITH_TRIGGER);
        wallpostProcessor = new VkWallpostProcessor(vkApiService, broadcastService, memory, vkConfig);
    }

    @Test
    public void testWallpostWithTrigger() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TEXT_WITH_TRIGGER);
        wallpost.setPostType(PostType.POST);

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage(TEXT_WITH_TRIGGER, null);

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1)).broadcast(eq(broadcastMessage));
        Mockito.verifyNoInteractions(vkApiService);
    }

    @Test
    public void testWallpostWithoutTrigger() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TEXT_WITHOUT_TRIGGER);
        wallpost.setPostType(PostType.POST);

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage(TEXT_WITHOUT_TRIGGER, null);

        assertFalse(memory.contains(broadcastMessage));
        Mockito.verifyNoInteractions(broadcastService);
        Mockito.verifyNoInteractions(vkApiService);
    }

    @Test
    public void testWallpostWithTriggerAndPhotoAttachment() throws MalformedURLException, URISyntaxException {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TEXT_WITH_TRIGGER);
        wallpost.setPostType(PostType.POST);

        WallpostAttachment photoAttachment = new WallpostAttachment();

        photoAttachment.setType(WallpostAttachmentType.PHOTO);
        Photo photo = new Photo();
        photo.setOwnerId(1);
        photo.setId(1);
        PhotoSizes photoSizeM = new PhotoSizes();
        photoSizeM.setType(PhotoSizesType.M);
        URL urlM = new URL("https://test.com/M");
        photoSizeM.setUrl(urlM.toURI());
        PhotoSizes photoSizeZ = new PhotoSizes();
        photoSizeZ.setType(PhotoSizesType.Z);
        URI urlZ = URI.create("https://test.com/Z");
        photoSizeZ.setUrl(urlZ);
        photoAttachment.setPhoto(photo);
        WallpostAttachment notPhotoAttachment = new WallpostAttachment();
        notPhotoAttachment.setType(WallpostAttachmentType.GRAFFITI);

        wallpost.setAttachments(List.of(photoAttachment, notPhotoAttachment));

        List<String> photoIdsList = List.of(String.format("%s_%s", photo.getOwnerId(), photo.getId()));
        GetByIdLegacyResponse photoResponse = new GetByIdLegacyResponse();
        photoResponse.setId(1);
        photoResponse.setOwnerId(1);
        photoResponse.setSizes(List.of(photoSizeM, photoSizeZ));
        List<GetByIdLegacyResponse> photosGetByIdResponse = List.of(photoResponse);


        Mockito.when(vkApiService.getPhotoById(photoIdsList)).thenReturn(photosGetByIdResponse);
        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage(TEXT_WITH_TRIGGER, List.of(urlZ.toURL()));

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1)).broadcast(eq(broadcastMessage));
        Mockito.verify(vkApiService, Mockito.times(1)).getPhotoById(photoIdsList);
    }

    @Test
    public void testWallpostWithTriggerWithoutPhotoAttachment() {
        Wallpost wallpost = new Wallpost();
        wallpost.setText(TEXT_WITH_TRIGGER);
        wallpost.setPostType(PostType.POST);

        WallpostAttachment notPhotoAttachment = new WallpostAttachment();
        notPhotoAttachment.setType(WallpostAttachmentType.GRAFFITI);

        wallpost.setAttachments(List.of(notPhotoAttachment));

        wallpostProcessor.process(wallpost);

        BroadcastMessage broadcastMessage = new BroadcastMessage(TEXT_WITH_TRIGGER, null);

        assertTrue(memory.contains(broadcastMessage));
        Mockito.verify(broadcastService, Mockito.times(1)).broadcast(eq(broadcastMessage));
        Mockito.verifyNoInteractions(vkApiService);
    }
}
