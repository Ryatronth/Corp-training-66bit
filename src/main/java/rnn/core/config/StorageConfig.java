package rnn.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class StorageConfig {
    private static final String COURSE_PATH = "courses";
    private static final String CONTENT_PATH = "content";

    @Qualifier("fileStorageWebClient")
    private final WebClient webClient;

    public StorageConfig(@Qualifier("fileStorageWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String uploadCourseImage(UUID courseImageUUID, MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("path", COURSE_PATH);
        builder.part("name", courseImageUUID.toString());
        builder.part("file", file.getResource());
        MultiValueMap<String, HttpEntity<?>> multipartData = builder.build();

        return webClient
                .post()
                .uri("/api/v1/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String uploadContentFile(long topicId, UUID contentImageUUID, MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("path", "%s/%s".formatted(CONTENT_PATH, topicId));
        builder.part("name", contentImageUUID.toString());
        builder.part("file", file.getResource());
        MultiValueMap<String, HttpEntity<?>> multipartData = builder.build();

        return webClient
                .post()
                .uri("/api/v1/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
