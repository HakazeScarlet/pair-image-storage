package com.github.hakazescarlet.pairimagestorage.pair_image;

import com.github.hakazescarlet.pairimagestorage.http_client.ImageHttpRequestSender;
import com.github.hakazescarlet.pairimagestorage.utils.MediaTypeResolver;
import jakarta.annotation.PreDestroy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/")
public class ImageController {

    private final ImageHttpRequestSender imageHttpRequestSender;
    private final PairImageService pairImageService;

    public ImageController(ImageHttpRequestSender imageHttpRequestSender, PairImageService pairImageService) {
        this.imageHttpRequestSender = imageHttpRequestSender;
        this.pairImageService = pairImageService;
    }

    @PostMapping("/send_image")
    public ResponseEntity<byte[]> getImage(@RequestParam("image") MultipartFile image) {
        HttpResponse<byte[]> response = imageHttpRequestSender.send(image);
        byte[] body = response.body();

        Map<String, List<String>> headers = response.headers().map();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());

        try {
            pairImageService.save(new RawImagesHolder(image.getBytes(), body, image.getContentType()));
        } catch (IOException e) {
            throw new MultipartFileException("Could not save file: " + image.getOriginalFilename(), e);
        }

        if (response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
        } else {
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers.get(HttpHeaders.CONTENT_DISPOSITION).getFirst())
                .header(HttpHeaders.CONTENT_TYPE, MediaTypeResolver.resolve(extension))
                .body(body);
        }
    }

    @PreDestroy
    public void deleteDirectory() {
        Path path = Paths.get("temp");
        try (Stream<Path> deletedFiles = Files.walk(path)) {
            deletedFiles
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            throw new DirectoryDeletingException("Failed to delete directory " + path, e);
        }
    }

    private static class MultipartFileException extends RuntimeException {
        public MultipartFileException(String message, Exception e) {
            super(message, e);
        }
    }

    private static class DirectoryDeletingException extends RuntimeException {
        public DirectoryDeletingException(String message, Exception e) {
            super(message, e);
        }
    }
}
