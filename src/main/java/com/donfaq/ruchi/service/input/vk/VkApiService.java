package com.donfaq.ruchi.service.input.vk;

import com.donfaq.ruchi.config.properties.VkConfigProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetByIdLegacyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VkApiService {
    private final VkConfigProperties vkConfig;
    private final VkApiClient vkApiClient;

    @Autowired
    public VkApiService(VkConfigProperties vkConfig, VkApiClient vkApiClient) {
        this.vkConfig = vkConfig;
        this.vkApiClient = vkApiClient;
    }

    //    @SneakyThrows
    public List<GetByIdLegacyResponse> getPhotoById(List<String> photos) {
        try {
            return vkApiClient.photos()
                    .getByIdLegacy(
                            new ServiceActor(null, vkConfig.serviceAccessToken()), photos)
                    .execute();
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
