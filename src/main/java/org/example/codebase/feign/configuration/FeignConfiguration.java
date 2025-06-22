package org.example.codebase.feign.configuration;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.example.codebase.feign.exception.FeignError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FeignConfiguration.class);

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer your-token-here");
            requestTemplate.header("X-Trace-Id", "trace-" + System.currentTimeMillis());

            log.info("[Feign] Sending request to: {}", requestTemplate.url());
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignError();
    }
}
