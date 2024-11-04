package rnn.filestorage.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {
    @Value("${spring.web.resources.static-locations}")
    protected String staticLocation;

    @Value("${server.api_v1}")
    private String apiV1;

    @PostConstruct
    public void initDefaultPath() throws IOException {
        Path path = Path.of(staticLocation).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    protected void initPath(String path) throws IOException {
        Path created = Path.of(staticLocation, path).toAbsolutePath().normalize();
        if (!Files.exists(created)) {
            Files.createDirectories(created);
        }
    }

    public String uploadFile(String path, String name, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не имеет содержимого");
        }

        initPath(path);

        String contentType = file.getContentType();
        createFile(path, name, file, contentType.substring(contentType.lastIndexOf('/') + 1));

        return "%s/files?path=%s&name=%s".formatted(apiV1, path, name);
    }

    protected void createFile(String path, String name, MultipartFile file, String contentType) throws IOException {
        String filename = "%s.%s".formatted(name, contentType);
        Path fullPath = Path.of(staticLocation, path, filename).toAbsolutePath().normalize();

        byte[] bytes = file.getBytes();

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fullPath.toString()))) {
            stream.write(bytes);
        }
    }

    public UrlResource getFile(String path, String name) throws IOException {
        Path pathToFile = Path.of(staticLocation, path).normalize();
        File[] files = findFiles(pathToFile, name);

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("Файл не найден. URL: %s/files?path=%s&name=%s".formatted(apiV1, path, name));
        }

        return new UrlResource(Path.of(files[0].getPath()).toUri());
    }

    protected File[] findFiles(Path path, String filename) {
        return path.toFile().listFiles((dir, name) -> name.startsWith("%s".formatted(filename)));
    }
}
