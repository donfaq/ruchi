package com.donfaq.ruchi.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        return execution.execute(request, body);

    }

    private void traceRequest(HttpRequest request, byte[] body) {
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        if (headers.containsKey("Authorization")) {
            headers.put("Authorization", "<token>");
        }
        log.info("{} {} headers: {} payload: {}", request.getMethod(), request.getURI(), headers, new String(body, UTF_8));
    }
}
