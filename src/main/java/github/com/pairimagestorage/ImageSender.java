package github.com.pairimagestorage;

import github.com.pairimagestorage.exception.IOResourceException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ImageSender {

    private final HttpClient httpClient;

    public ImageSender(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void sendRequest(MultipartFile image) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/convert_image"))
                    .headers("Content-Type", image.getContentType())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(image.getBytes()))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException e) {
            throw new IOResourceException("Invalid request form or unable to extract data from response", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
