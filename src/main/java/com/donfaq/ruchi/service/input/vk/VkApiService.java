package com.donfaq.ruchi.service.input.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.objects.photos.Photo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VkApiService {
    private final String serviceAccessToken;
    private final VkApiClient vkApiClient;

    public VkApiService(@Value("${vk.serviceAccessToken}") String serviceAccessToken, VkApiClient vkApiClient) {
        this.serviceAccessToken = serviceAccessToken;
        this.vkApiClient = vkApiClient;
    }

    public List<Photo> updatePhotoInfo(List<Photo> photos) {
        return getPhotoById(
                photos.stream()
                      .map(photo -> String.format("%s_%s", photo.getOwnerId(), photo.getId()))
                      .collect(Collectors.toList())
        );
    }

    @SneakyThrows
    public List<Photo> getPhotoById(List<String> photos) {
        return vkApiClient.photos()
                          .getById(new ServiceActor(null, serviceAccessToken), photos)
                          .execute();
    }
}
