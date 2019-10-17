package com.donfaq.ruchi.integration.service.input;

import com.donfaq.ruchi.integration.model.InputType;

public interface InputService {
    Object process(InputType inputMessage);
}
