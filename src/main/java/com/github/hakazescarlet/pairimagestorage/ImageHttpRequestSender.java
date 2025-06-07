package com.github.hakazescarlet.pairimagestorage;

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
import java.util.Objects;

@Component
public class ImageHttpRequestSender {

    private static final int STATUS_CODE_OK = 200;

    private final HttpClient httpClient;

    public ImageHttpRequestSender(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpResponse<byte[]> send(MultipartFile image) {
        Path path = Paths.get("temp");
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new DirectoryCreatingException("Failed to create directory /temp", e);
            }
        }

        Path tempDir = path
                .resolve(Objects.requireNonNull(image.getOriginalFilename()))
                .normalize()
                .toAbsolutePath();
        try {
            Files.copy(image.getInputStream(), tempDir);
        } catch (IOException e) {
            throw new ImageSavingException("Unable to save image " + image.getName(), e);
        }

        try {
            File file = new File(tempDir.toString());
            HTTPRequestMultipartBody multipartBody = new HTTPRequestMultipartBody.Builder()
                    .addPart("image", file, image.getContentType(), image.getOriginalFilename())
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/convert_image"))
                    .headers("Content-Type", multipartBody.getContentType())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody.getBody()))
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (STATUS_CODE_OK == response.statusCode()) {
                return response;
            }
            return new StatusCodeHttpResponse(response.statusCode());
        } catch (IOException e) {
            throw new IOResourceException("Unable to extract data from response or send request to server", e);
        } catch (InterruptedException e) {
            throw new ResponseReturnException("Failed to get response", e);
        }
    }

    private static class DirectoryCreatingException extends RuntimeException {
        public DirectoryCreatingException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class ImageSavingException extends RuntimeException {
        ImageSavingException(String message, Exception e) {
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
}
