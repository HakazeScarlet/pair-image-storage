package com.github.hakazescarlet.pairimagestorage.image_storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class PairImage {

    @Id
    private String id;

    private ColorImage colorImage;
    private GrayImage grayImage;

    public PairImage(ColorImage colorImage, GrayImage grayImage) {
        this.colorImage = colorImage;
        this.grayImage = grayImage;
    }

    public ColorImage getColorImage() {
        return colorImage;
    }

    public void setColorImage(ColorImage colorImage) {
        this.colorImage = colorImage;
    }

    public GrayImage getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(GrayImage grayImage) {
        this.grayImage = grayImage;
    }
}
