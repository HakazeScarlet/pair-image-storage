package com.github.hakazescarlet.pairimagestorage.pair_image;

import org.bson.types.Binary;
import org.springframework.stereotype.Service;

@Service
public class PairImageService {

    private final PairImageRepository pairImageRepository;

    public PairImageService(PairImageRepository pairImageRepository) {
        this.pairImageRepository = pairImageRepository;
    }

    public String save(RawImagesHolder rawImages) {
        PairImage images = new PairImage();
        images.setColorfulImage(new Binary(rawImages.getColorful()));
        images.setGrayImage(new Binary(rawImages.getGray()));
        images.setContentType(rawImages.getContentType());
        pairImageRepository.insert(images);
        return images.getId();
    }
}
