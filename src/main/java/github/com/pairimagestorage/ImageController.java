package github.com.pairimagestorage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class ImageController {

    private final ImageSender imageSender;

    public ImageController(ImageSender imageSender) {
        this.imageSender = imageSender;
    }

    @PostMapping("/send_image")
    public void getImage(@RequestParam("image") MultipartFile image) {
        imageSender.sendRequest(image);
    }
}
