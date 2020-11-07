package com.donfaq.ruchi.service.input.vk;

import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.BroadcastService;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VkWallpostProcessor {
    private final String triggerString;
    private final VkApiService vkApiService;
    private final BroadcastService broadcastService;
    private final BlockingMemory memory;

    public VkWallpostProcessor(
            @Value("${vk.triggerString}") String triggerString,
            VkApiService vkApiService,
            BroadcastService broadcastService,
            BlockingMemory memory
    ) {
        this.triggerString = triggerString;
        this.vkApiService = vkApiService;
        this.broadcastService = broadcastService;
        this.memory = memory;
    }


    private Optional<URL> getLargestPhotoUrl(Photo photo) {
        return photo
                .getSizes()
                .stream()
                .max(Comparator.comparing(PhotoSizes::getType))
                .map(PhotoSizes::getUrl);
    }

    private List<URL> processImages(List<WallpostAttachment> attachments) {
        List<URL> result = null;
        List<Photo> photos = attachments.stream()
                                        .filter(attach -> attach.getType().equals(WallpostAttachmentType.PHOTO))
                                        .map(WallpostAttachment::getPhoto)
                                        .collect(Collectors.toList());
        if (!photos.isEmpty()) {
            photos = vkApiService.updatePhotoInfo(photos);
            result = photos.stream()
                           .map(this::getLargestPhotoUrl)
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                           .collect(Collectors.toList());
        }
        return result;
    }

    public void process(Wallpost wallpost) {
        if (!wallpost.getPostType().equals(PostType.POST)
                && !wallpost.getPostType().equals(PostType.POSTPONE)) {
            log.info("Received VK wallpost type isn't acceptable: {}", wallpost.getPostType());
            return;
        }

        if (wallpost.getText() != null && wallpost.getText().contains(triggerString)) {
            BroadcastMessage message = new BroadcastMessage();
            message.setText(wallpost.getText());

            if (wallpost.getAttachments() != null && !wallpost.getAttachments().isEmpty()) {
                message.setImages(processImages(wallpost.getAttachments()));
            }

            if (this.memory.contains(message)) {
                log.info("Received VK wallpost that has already been processed");
                return;
            }
            this.memory.add(message);
            broadcastService.broadcast(message);

        } else {
            log.info("Received VK wallpost text is empty or doesn't contain trigger string");
        }
    }
}
