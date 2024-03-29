package com.donfaq.ruchi.service.input.vk;

import com.donfaq.ruchi.component.BlockingMemory;
import com.donfaq.ruchi.config.properties.VkConfigProperties;
import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.service.BroadcastService;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.responses.GetByIdLegacyResponse;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VkWallpostProcessor {
    private final VkApiService vkApiService;
    private final BroadcastService broadcastService;
    private final BlockingMemory memory;
    private final VkConfigProperties vkConfig;
    private final Logger log = LoggerFactory.getLogger(VkWallpostProcessor.class);

    public VkWallpostProcessor(VkApiService vkApiService, BroadcastService broadcastService, BlockingMemory memory, VkConfigProperties vkConfig) {
        this.vkApiService = vkApiService;
        this.broadcastService = broadcastService;
        this.memory = memory;
        this.vkConfig = vkConfig;
    }

    private Optional<URI> getLargestPhotoUri(GetByIdLegacyResponse photo) {
        return photo
                .getSizes()
                .stream()
                .max(Comparator.comparing(PhotoSizes::getType))
                .map(PhotoSizes::getUrl);
    }

    private Optional<URL> toURL(URI uri) {
        Optional<URL> url = Optional.empty();
        try {
            url = Optional.of(uri.toURL());
        } catch (MalformedURLException e) {
            log.warn("Incorrect photo URL: {}", uri);
        }
        return url;
    }

//    @SneakyThrows

    private List<URL> extractPhotoAttachments(List<WallpostAttachment> attachments) {
        List<URL> result = null;
        List<String> photoIds = attachments
                .stream()
                .filter(attach -> attach.getType().equals(WallpostAttachmentType.PHOTO))
                .map(WallpostAttachment::getPhoto)
                .map(photo -> String.format("%s_%s", photo.getOwnerId(), photo.getId()))
                .collect(Collectors.toList());

        if (!photoIds.isEmpty()) {
            result = vkApiService
                    .getPhotoById(photoIds)
                    .stream()
                    .map(this::getLargestPhotoUri)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(this::toURL)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        return result;
    }

    public void process(Wallpost wallpost) {
        log.info("Processing new wallpost");
        if (!wallpost.getPostType().equals(PostType.POST)
                && !wallpost.getPostType().equals(PostType.POSTPONE)) {
            log.info("Received VK wallpost type isn't acceptable: {}", wallpost.getPostType());
            return;
        }

        if (wallpost.getText() != null && wallpost.getText().contains(this.vkConfig.triggerString())) {
            BroadcastMessage message = new BroadcastMessage(wallpost.getText(), null);

            if (wallpost.getAttachments() != null && !wallpost.getAttachments().isEmpty()) {
                message = new BroadcastMessage(wallpost.getText(), extractPhotoAttachments(wallpost.getAttachments()));
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
