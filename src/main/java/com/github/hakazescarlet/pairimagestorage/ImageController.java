package com.github.hakazescarlet.pairimagestorage;

import com.github.hakazescarlet.pairimagestorage.image_storage.Image;
import com.github.hakazescarlet.pairimagestorage.image_storage.ImageService;
import com.github.hakazescarlet.pairimagestorage.image_storage.PairImage;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
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
    private final ImageService imageService;

    public ImageController(ImageHttpRequestSender imageHttpRequestSender, ImageService imageService) {
        this.imageHttpRequestSender = imageHttpRequestSender;
        this.imageService = imageService;
    }

    @PostMapping("/send_image")
    public ResponseEntity<byte[]> getImage(@RequestParam("image") MultipartFile image) {
        HttpResponse<byte[]> response = imageHttpRequestSender.send(image);
        byte[] body = response.body();

        try {
            Image colorfulImage = new Image(image.getOriginalFilename(), image.getBytes());
            Image grayImage = new Image("gray" + image.getOriginalFilename(), body);
            PairImage pairImage = new PairImage(colorfulImage, grayImage, "pair" + colorfulImage.getName());

            if (pairImage.getColorfulImage() != null && pairImage.getGrayImage() != null) {
                MongoCollection<Document> imagePairCollection = database.getCollection("image_pairs");
                Document imagePairDocument = new Document("image1_id", imageIds.get(0)).append("image2_id", imageIds.get(1));
                imagePairCollection.insertOne(imagePairDocument);
                System.out.println("Image pair saved with IDs: " + imageIds.get(0) + " and " + imageIds.get(1));
            } else {
                System.err.println("Error: Could not save image pair.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<String>> headers = response.headers().map();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());

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
