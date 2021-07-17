package com.donfaq.ruchi.service.input.vk;

import com.donfaq.ruchi.config.properties.VkConfigProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.objects.photos.responses.GetByIdLegacyResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VkApiService {
    private final VkConfigProperties vkConfig;
    private final VkApiClient vkApiClient;

    @SneakyThrows
    public List<GetByIdLegacyResponse> getPhotoById(List<String> photos) {
        return vkApiClient.photos()
                          .getByIdLegacy(
                                  new ServiceActor(null, vkConfig.getServiceAccessToken()), photos)
                          .execute();
    }
}
