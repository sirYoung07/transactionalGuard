package com.market.transactionguard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MonnifyWebClientConfig {
    private static final String MONNIFY_BASE_URL = "https://sandbox.monnify.com";

    @Bean
    public WebClient monnifyWebClient() {
        return WebClient.builder()
            .baseUrl(MONNIFY_BASE_URL)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Authorization", "Bearer yourAccessToken")
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build())
            .build();
    }
}
