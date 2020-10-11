package com.donfaq.ruchi.service.input;

import com.donfaq.ruchi.model.BroadcastMessage;
import com.donfaq.ruchi.model.InputType;
import com.donfaq.ruchi.model.vk.VkInputType;
import com.donfaq.ruchi.model.vk.api.Photo;
import com.donfaq.ruchi.model.vk.api.PhotoSizes;
import com.donfaq.ruchi.model.vk.api.Wallpost;
import com.donfaq.ruchi.service.broadcast.BroadcastService;
import com.donfaq.ruchi.util.BlockingMemory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkInputService {

    @Value("${vk.confirmationCode}")
    private String confirmationCode;

    @Value("${vk.serviceAccessToken}")
    private String serviceAccessToken;

    @Value("${vk.triggerString}")
    private String triggerString;

    private static final String VK_API_VERSION = "5.124";
    private static final String VK_BASE_URI = "https://api.vk.com/method/";

    private final RestTemplate restTemplate;
    private final BroadcastService broadcastService;
    private final BlockingMemory memory;

    private boolean isContainsPhotoAttachment(Wallpost wallpost) {
        boolean result = false;

        if (wallpost.getAttachments() != null) {
            result = wallpost
                    .getAttachments()
                    .stream()
                    .anyMatch(attachment -> attachment.getType().equals("photo"));
        }
        return result;
    }

    private String constructPhotoId(Photo photo) {
        return photo.getOwnerId() + "_" + photo.getId() + "_" + photo.getAccessKey();

    }

    private Optional<URL> getLargestPhotoUrl(Photo photo) {
        return photo
                .getSizes()
                .stream()
                .max(Comparator.comparing(PhotoSizes::getType))
                .map(PhotoSizes::getUrl);
    }

    private List<URL> getPostImages(Wallpost wallpost) {
        List<URL> result = new ArrayList<>();

        String photos = wallpost
                .getAttachments()
                .stream()
                .filter(attachment -> "photo".equals(attachment.getType()))
                .map(attachment -> constructPhotoId(attachment.getPhoto()))
                .collect(Collectors.joining(","));

        Optional<List<Photo>> photoDetails = getPhotoDetails(photos);

        if (photoDetails.isPresent()) {
            result = photoDetails
                    .get()
                    .stream()
                    .map(this::getLargestPhotoUrl)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public String getText(Wallpost wallpost) {
        log.info("Extracting message text from callback");
        String text = "";

        if (!"".equals(wallpost.getText()) && wallpost.getText() != null) {
            text += wallpost.getText();
        }

        return text;
    }

    public Optional<List<Photo>> getPhotoDetails(String photos) {
        String uri = VK_BASE_URI + "photos.getById";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                                                           .queryParam("v", VK_API_VERSION)
                                                           .queryParam("access_token", serviceAccessToken)
                                                           .queryParam("photos", photos)
                                                           .queryParam("photo_sizes", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, List<Photo>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        Optional<List<Photo>> result = Optional.empty();
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            result = Optional.of(response.getBody().get("response"));
        }

        return result;
    }


    private void processNewWallpost(Wallpost wallpost) {
        String wallpostText = getText(wallpost);

        if (wallpostText.contains(this.triggerString)) {

            BroadcastMessage message = new BroadcastMessage();
            message.setText(wallpostText);

            if (isContainsPhotoAttachment(wallpost)) {
                message.setImages(getPostImages(wallpost));
            }

            if (this.memory.contains(message)) {
                log.info("Received VK wallpost that has already been processed");
                return;
            }
            this.memory.add(message);

            broadcastService.broadcast(message);

        } else {
            log.info("Received VK wallpost doesn't contain trigger string");
        }
    }


    public String process(InputType inputMessage) {
        VkInputType callback = (VkInputType) inputMessage;
        String result = "ok";
        if ("confirmation".equals(callback.getType())) {
            log.info("Confirmation request");
            result = this.confirmationCode;
        } else if ("wall_post_new".equals(callback.getType())) {
            log.info("Processing new wallpost");
            processNewWallpost(callback.getObject());
        }
        return result;
    }
}
