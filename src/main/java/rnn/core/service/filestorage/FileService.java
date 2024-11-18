package rnn.core.service.filestorage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.model.admin.Course;
import rnn.core.config.StorageConfig;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileService {
    private final StorageConfig storageConfig;

    public String createCourseImage(UUID uuid, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Изображение не может быть пустым.");
        }

        return storageConfig.uploadCourseImage(uuid, file);
    }

    public String updateCourseImage(Course course, MultipartFile file) {
        String pictureUrl = course.getPictureUrl();

        if (pictureUrl != null) {
            UUID uuid = extractUUID(pictureUrl);
            return createCourseImage(uuid, file);
        }

        return null;
    }

    protected UUID extractUUID(String imageUrl) {
        return UUID.fromString(imageUrl.substring(imageUrl.length() - 36));
    }
}
