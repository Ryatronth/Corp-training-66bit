package rnn.core.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

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
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                                .responseTimeout(Duration.ofSeconds(10))
                ))
                .build();
    }
}
