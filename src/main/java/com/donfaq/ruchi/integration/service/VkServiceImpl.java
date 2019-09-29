package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.vk.*;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VkServiceImpl implements VkService {
    private final Logger log = LoggerFactory.getLogger(VkServiceImpl.class);

    @Value("${vk.confirmationCode}")
    private String confirmationCode;

    @Value("${vk.serviceAccessToken}")
    private String vkServiceAccessToken;

    private static final String VK_API_VERSION = "5.77";
    private static final String VK_BASE_URI = "https://api.vk.com/method/";

    private RestTemplate restTemplate;

    @Autowired
    public VkServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @Override
    public boolean isConfirmation(Callback callback) {
        return "confirmation".equals(callback.getType());
    }

    @Override
    public String getConfirmationCode() {
        return this.confirmationCode;
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

    private String getLargestPhotoUrl(Photo photo) {
        String result = "";
        for (String sizeType : Arrays.asList("w", "z", "y", "x", "m", "s")) {
            Optional<PhotoSizes> largestSize = photo
                    .getSizes()
                    .stream()
                    .filter(sizes -> sizeType.equals(sizes.getType()))
                    .findAny();
            if(largestSize.isPresent()) {
                return largestSize.get().getUrl().toString();
            }
        }
        return result;
    }

    private String getPhotoText(Wallpost wallpost) {
        String photos = wallpost.getAttachments()
                .stream()
                .filter(attachment -> "photo".equals(attachment.getType()))
                .map(attachment -> constructPhotoId(attachment.getPhoto()))
                .collect(Collectors.joining(","));

        return getPhotoDetails(photos)
                .stream()
                .map(this::getLargestPhotoUrl)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getText(Callback callback) {
        log.info("Extracting message text from callback");
        String text = "";
        Wallpost wallpost = callback.getObject();

        if (isContainsText(wallpost)) {
            text += wallpost.getText();
        }

        if (isContainsPhotoAttachment(wallpost)) {
            text += "\n\n" +  getPhotoText(wallpost);
        }

        return text;
    }

    public List<Photo> getPhotoDetails(String photos) {
        String uri = VK_BASE_URI + "photos.getById";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("v", VK_API_VERSION)
                .queryParam("access_token", vkServiceAccessToken)
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

        return response.getBody().get("response");


    }
}
