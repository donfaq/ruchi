package com.donfaq.ruchi.component.converters;

import com.donfaq.ruchi.model.twitch.websub.WebSubHubMode;
import org.springframework.core.convert.converter.Converter;

public class StringToWebSubHubModeConverter implements Converter<String, WebSubHubMode> {
    @Override
    public WebSubHubMode convert(String source) {
        return WebSubHubMode.valueOf(source.toUpperCase());
    }
}
