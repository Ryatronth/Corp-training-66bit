package rnn.core.filestorage;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class FileStorage {
    @Qualifier("fileStorageWebClient")
    private final WebClient webClient;

    public FileStorage(@Qualifier("fileStorageWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String uploadCourseImage(UUID courseImageUUID, MultipartFile file) {
        return webClient
                .post()
                .uri("/api/v1/courses/{uuid}", courseImageUUID)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromMultipartData("file", file))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class);
                    }
                    throw new ServiceException("Ошибка при отправке файла: status code %s".formatted(response.statusCode()));
                }).block();
    }
}
