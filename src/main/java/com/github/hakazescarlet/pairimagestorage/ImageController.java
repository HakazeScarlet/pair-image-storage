package com.github.hakazescarlet.pairimagestorage;

import com.github.hakazescarlet.pairimagestorage.image_storage.PairImageService;
import com.github.hakazescarlet.pairimagestorage.image_storage.RawImagesHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping("/send_image")
    public ResponseEntity<byte[]> getImage(@RequestParam("image") MultipartFile image) {
        HttpResponse<byte[]> response = imageHttpRequestSender.send(image);
        byte[] body = response.body();

        Map<String, List<String>> headers = response.headers().map();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());

        try {
            pairImageService.save(new RawImagesHolder(image.getBytes(), body, image.getContentType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == StatusCodeHttpResponse.SERVER_ERROR_CODE) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(body);
        } else {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, headers.get(HttpHeaders.CONTENT_DISPOSITION).getFirst())
                    .header(HttpHeaders.CONTENT_TYPE, getMediaType(extension))
                    .body(body);
        }
    }

    private String getMediaType(String extension) {
        if ("gif".equals(extension)) {
            return MediaType.IMAGE_GIF_VALUE;
        } else if ("jpeg".equals(extension)) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else {
            return MediaType.IMAGE_PNG_VALUE;
        }
    }
}
