package com.github.hakazescarlet.pairimagestorage.http_client;

import com.github.hakazescarlet.pairimagestorage.configuration.PairImageStorageConfiguration;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class ImageHttpRequestSender {

    private static final String TEMP_DIRECTORY = "temp";

    @Value("${server.url}")
    private String serverUrl;

    private final HttpClient httpClient;

    public ImageHttpRequestSender(HttpClient httpClient) {
        this.httpClient = httpClient;

        Path path = Paths.get(TEMP_DIRECTORY);
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new CreateDirectoryException("Failed to create directory /temp", e);
            }
        }
    }

    public HttpResponse<byte[]> send(MultipartFile image) {
        Path path = Paths.get(TEMP_DIRECTORY);
        Path tempDir = path
            .resolve(Objects.requireNonNull(image.getOriginalFilename()))
            .normalize()
            .toAbsolutePath();
        try {
            Files.copy(image.getInputStream(), tempDir);
        } catch (IOException e) {
            throw new SaveImageException("Unable to save image " + image.getName(), e);
        }

        try {
            File file = new File(tempDir.toString());
            HttpRequestMultipartBody multipartBody = new HttpRequestMultipartBody.Builder()
                .addPart(
                    PairImageStorageConfiguration.IMAGE_KEY,
                    file,
                    image.getContentType(),
                    image.getOriginalFilename()
                )
                .build();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .headers("Content-Type", multipartBody.getContentType())
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody.getBody()))
                .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (HttpStatus.OK.value() == response.statusCode()) {
                return response;
            }
            return response;
        } catch (IOException e) {
            throw new IOResourceException("Unable to extract data from response or send request to server", e);
        } catch (InterruptedException e) {
            throw new ResponseReturnException("Failed to get response", e);
        }
    }

    @PreDestroy
    public void removeDirectory() {
        Path path = Paths.get("temp");
        try (Stream<Path> deletedFiles = Files.walk(path)) {
            deletedFiles
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            throw new RemoveDirectoryException("Failed to delete directory " + path, e);
        }
    }

    private static class CreateDirectoryException extends RuntimeException {
        public CreateDirectoryException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class SaveImageException extends RuntimeException {
        SaveImageException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class IOResourceException extends RuntimeException {
        public IOResourceException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class ResponseReturnException extends RuntimeException {
        public ResponseReturnException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class RemoveDirectoryException extends RuntimeException {
        public RemoveDirectoryException(String message, Exception e) {
            super(message, e);
        }
    }
}
