package rnn.filestorage.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rnn.filestorage.service.exception.exception.FileIsEmptyException;
import rnn.filestorage.service.exception.exception.UnsupportedFileTypeException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileService {
    @Value("${spring.web.resources.static-locations}")
    protected String staticLocation;

    @Value("${path.courses}")
    protected String courseLocation;

    @Value("${server.api_v1}")
    private String apiV1;

    @PostConstruct
    public void initPaths() throws IOException {
        Path path = Path.of(staticLocation).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        Path pathToCourses = path.resolve(courseLocation);
        if (!Files.exists(pathToCourses)) {
            Files.createDirectory(pathToCourses);
        }
    }

    public String uploadCourseFile(UUID uuid, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileIsEmptyException("Файл не имеет содержимого");
        }

        String contentType = file.getContentType();

        return switch (contentType) {
            case "image/jpeg", "image/png" -> {
                createFile(uuid, file, contentType.substring(contentType.lastIndexOf('/') + 1));
                yield "%s/courses/%s".formatted(apiV1, uuid);
            }
            default -> throw new UnsupportedFileTypeException("Неподдерживаемое расширение файла {%s}".formatted(contentType));
        };
    }

    protected void createFile(UUID uuid, MultipartFile file, String contentType) throws IOException {
        String filename = "%s.%s".formatted(uuid, contentType);
        Path fullPath = Path.of(staticLocation, courseLocation, filename).toAbsolutePath().normalize();

        byte[] bytes = file.getBytes();

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fullPath.toString()))) {
            stream.write(bytes);
        }

    }


    public Resource getCourseFile(UUID uuid) throws IOException {
        Path path = Path.of(staticLocation, courseLocation).normalize();
        File[] files = findFiles(uuid, path);

        if (files.length == 0) {
            throw new FileNotFoundException("Файл не найден. URL: %s/courses/%s".formatted(apiV1, uuid));
        }

        return new UrlResource(Path.of(files[0].getPath()).toUri());
    }

    protected File[] findFiles(UUID uuid, Path path) {
        return path.toFile().listFiles((dir, name) -> name.startsWith("%s".formatted(uuid)));
    }
}
