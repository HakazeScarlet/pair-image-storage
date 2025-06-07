package com.github.hakazescarlet.pairimagestorage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ImageController {

    private final ImageHttpRequestSender imageHttpRequestSender;

    public ImageController(ImageHttpRequestSender imageHttpRequestSender) {
        this.imageHttpRequestSender = imageHttpRequestSender;
    }

    @PostMapping("/send_image")
    public ResponseEntity<byte[]> getImage(@RequestParam("image") MultipartFile image) {
        HttpResponse<byte[]> response = imageHttpRequestSender.send(image);
        byte[] body = response.body();
        Map<String, List<String>> headers = response.headers().map();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        // TODO: handle 500 status code
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headers.get(HttpHeaders.CONTENT_DISPOSITION).getFirst())
                .header(HttpHeaders.CONTENT_TYPE, getMediaType(extension))
                .body(body);
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
