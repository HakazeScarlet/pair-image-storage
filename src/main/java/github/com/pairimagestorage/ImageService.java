package github.com.pairimagestorage;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @PostMapping()
    public void sendImage(@RequestParam("image") MultipartFile image) {

    }
}
