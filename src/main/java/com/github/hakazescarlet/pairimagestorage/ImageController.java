package com.github.hakazescarlet.pairimagestorage;

import com.github.hakazescarlet.pairimagestorage.image_storage.PairImageService;
import com.github.hakazescarlet.pairimagestorage.image_storage.RawImagesHolder;
import com.github.hakazescarlet.pairimagestorage.utils.MediaTypeResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ImageController {

    private final ImageHttpRequestSender imageHttpRequestSender;
    private final PairImageService pairImageService;

    public ImageController(ImageHttpRequestSender imageHttpRequestSender, PairImageService pairImageService) {
        this.imageHttpRequestSender = imageHttpRequestSender;
        this.pairImageService = pairImageService;
    }

    @PostMapping("/images/convert")
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

        if (response.statusCode() == StatusCodeHttpResponse.SERVER_ERROR_CODE) {
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

    private static class MultipartFileException extends RuntimeException {
        public MultipartFileException(String message, Exception e) {
            super(message, e);
        }
    }
}
