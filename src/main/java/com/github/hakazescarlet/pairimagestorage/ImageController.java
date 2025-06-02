package com.github.hakazescarlet.pairimagestorage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class ImageController {

    private final ImageHttpRequestSender imageHttpRequestSender;

    public ImageController(ImageHttpRequestSender imageHttpRequestSender) {
        this.imageHttpRequestSender = imageHttpRequestSender;
    }

    @PostMapping("/send_image")
    public void getImage(@RequestParam("image") MultipartFile image) {
        imageHttpRequestSender.send(image);
    }
}
