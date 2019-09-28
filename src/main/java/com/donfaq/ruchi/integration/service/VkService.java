package com.donfaq.ruchi.integration.service;

import com.donfaq.ruchi.integration.model.vk.Callback;

public interface VkService {
    boolean isConfirmation(Callback callback);

    String getConfirmationCode();

    String getText(Callback callback);
}
