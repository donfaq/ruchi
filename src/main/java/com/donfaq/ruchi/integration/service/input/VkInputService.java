package com.donfaq.ruchi.integration.service.input;

import com.donfaq.ruchi.integration.model.InputType;
import com.donfaq.ruchi.integration.model.vk.VkBroadcastMessage;
import com.donfaq.ruchi.integration.model.vk.VkInputType;
import com.donfaq.ruchi.integration.model.vk.api.Photo;
import com.donfaq.ruchi.integration.model.vk.api.PhotoSizes;
import com.donfaq.ruchi.integration.model.vk.api.Wallpost;
import com.donfaq.ruchi.integration.service.broadcast.BroadcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VkInputService implements InputService {
    private final Logger log = LoggerFactory.getLogger(VkInputService.class);

    @Value("${vk.confirmationCode}")
    private String confirmationCode;

    @Value("${vk.serviceAccessToken}")
    private String serviceAccessToken;

    @Value("${TRIGGER_STRING}")
    private String triggerString;

    private static final String VK_API_VERSION = "5.77";
    private static final String VK_BASE_URI = "https://api.vk.com/method/";

    private RestTemplate restTemplate;
    private BroadcastService broadcastService;


    @Autowired
    public VkInputService(RestTemplateBuilder restTemplateBuilder, BroadcastService broadcastService) {
        this.restTemplate = restTemplateBuilder.build();
        this.broadcastService = broadcastService;
    }


    public boolean isConfirmation(VkInputType callback) {
        return "confirmation".equals(callback.getType());
    }

    private boolean isContainsText(Wallpost wallpost) {
        return !"".equals(wallpost.getText()) && wallpost.getText() != null;
    }

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
//        Set<String> sizes = ew HashSet<>(Arrays.asList("w", "z", "y", "x", "m", "s"));
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

        if (isContainsText(wallpost)) {
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
                new ParameterizedTypeReference<Map<String, List<Photo>>>() {
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

            VkBroadcastMessage broadcastMessage = new VkBroadcastMessage();
            broadcastMessage.setText(wallpostText);

            if (isContainsPhotoAttachment(wallpost)) {
                broadcastMessage.setImages(getPostImages(wallpost));
            }

            broadcastService.broadcast(broadcastMessage);

        } else {
            log.info("Received VK wallpost doesn't contain trigger string");
        }
    }

    @Override
    public String process(InputType inputMessage) {
        VkInputType callback = (VkInputType) inputMessage;
        String result = "ok";
        if (isConfirmation(callback)) {
            log.info("Confirmation request");
            result = this.confirmationCode;
        } else {
            log.info("Processing new wallpost");
            processNewWallpost(callback.getObject());
        }
        return result;
    }
}
