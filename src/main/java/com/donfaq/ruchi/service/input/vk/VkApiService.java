package com.donfaq.ruchi.service.input.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.objects.photos.responses.GetByIdResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VkApiService {
    private final String serviceAccessToken;
    private final VkApiClient vkApiClient;

    public VkApiService(@Value("${vk.serviceAccessToken}") String serviceAccessToken, VkApiClient vkApiClient) {
        this.serviceAccessToken = serviceAccessToken;
        this.vkApiClient = vkApiClient;
    }

    @SneakyThrows
    public List<GetByIdResponse> getPhotoById(List<String> photos) {
        return vkApiClient.photos()
                          .getById(new ServiceActor(null, serviceAccessToken), photos)
                          .execute();
    }
}
