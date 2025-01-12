package rnn.core.service.filestorage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.config.StorageConfig;
import rnn.core.model.admin.Course;

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
            UUID uuid = StorageConfig.extractFileNameUUID(pictureUrl);
            return createCourseImage(uuid, file);
        }

        return null;
    }

    public String createContentFile(UUID fileName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым.");
        }

        return storageConfig.uploadContentFile(fileName, file);
    }

    public String updateContentFile(MultipartFile file) {
        if (file != null) {
            return createContentFile(UUID.randomUUID(), file);
        }

        return null;
    }

    public void deleteCourseImage(String imageURL) {
        storageConfig.deleteCourseImage(imageURL);
    }

    public void deleteContentFile(String fileURL) {
        storageConfig.deleteContentFile(fileURL);
    }
}
