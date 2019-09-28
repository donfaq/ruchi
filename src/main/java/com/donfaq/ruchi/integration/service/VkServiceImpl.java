package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.vk.Callback;
import com.donfaq.ruchi.integration.model.vk.Photo;
import com.donfaq.ruchi.integration.model.vk.Wallpost;
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

import java.util.List;

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

    private String getPhotoText(Wallpost wallpost) {
        return "<photo url>";
    }

    @Override
    public String getText(Callback callback) {
        String text = "";
        Wallpost wallpost = callback.getObject();

        if (isContainsText(wallpost)) {
            text += wallpost.getText();
        }

        if (isContainsPhotoAttachment(wallpost)) {
            text += getPhotoText(wallpost);
        }

        return text;
    }

    public void getPhotoDetails(Photo photo) {
        String uri = VK_BASE_URI + "photos.getById";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("v", VK_API_VERSION)
                .queryParam("access_token", vkServiceAccessToken)
                .queryParam("photos", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);


        ResponseEntity<List<Photo>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Photo>>() {
                }
        );

        List<Photo> photos = response.getBody();

        if (photos != null) {
            for (Photo p : photos) {
                log.info(p.toString());
            }
        }

    }
}
