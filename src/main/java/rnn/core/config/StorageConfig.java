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
        return uploadFile(COURSE_PATH, courseImageUUID, file);
    }

    public String uploadContentFile(UUID contentImageUUID, MultipartFile file) {
        return uploadFile(CONTENT_PATH, contentImageUUID, file);
    }

    public void deleteCourseImage(String courseImageURL) {
        deleteFile(COURSE_PATH, courseImageURL);
    }

    public void deleteContentFile(String contentURL) {
        deleteFile(CONTENT_PATH, contentURL);
    }

    public static String extractFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.length() - 36);
    }

    public static UUID extractFileNameUUID(String imageUrl) {
        return UUID.fromString(extractFileName(imageUrl));
    }

    private String uploadFile(String path, UUID fileUUID, MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("path", path);
        builder.part("name", fileUUID.toString());
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

    private void deleteFile(String path, String fileURL) {
        webClient
                .delete()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/api/v1/files")
                                .queryParam("path", path)
                                .queryParam("name", extractFileName(fileURL))
                                .build()
                )
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
