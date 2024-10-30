package rnn.core.filestorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${file_server.url}")
    private String baseUrl;

    @Value("${file_server.api_header}")
    private String apiHeader;

    @Value("${file_server.api_key}")
    private String apiKey;

    @Bean
    public WebClient fileStorageWebClient() {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiHeader, apiKey)
                .build();
    }
}
