package com.github.hakazescarlet.pairimagestorage.image_storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class PairImage {

    @Id
    private String id;

    private Image colorfulImage;
    private Image grayImage;
    private String pairName;

    public PairImage(Image colorfulImage, Image grayImage, String pairName) {
        this.colorfulImage = colorfulImage;
        this.grayImage = grayImage;
        this.pairName = pairName;
    }

    public Image getColorfulImage() {
        return colorfulImage;
    }

    public void setColorfulImage(Image image) {
        this.colorfulImage = image;
    }

    public Image getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(Image grayImage) {
        this.grayImage = grayImage;
    }

    public String getPairName() {
        return "pair_" + colorfulImage.getName();
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }
}
