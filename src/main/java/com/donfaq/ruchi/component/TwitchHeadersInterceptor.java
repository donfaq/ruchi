package com.donfaq.ruchi.component;

import com.donfaq.ruchi.config.properties.TwitchConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TwitchConfigProperties.class)
public class TwitchHeadersInterceptor implements ClientHttpRequestInterceptor {

    private final TwitchConfigProperties twitchProperties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("Client-ID", twitchProperties.getCredentials().getClientId());
        return execution.execute(request, body);
    }
}
