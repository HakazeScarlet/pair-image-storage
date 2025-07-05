package com.github.hakazescarlet.pairimagestorage.pair_image;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PairImage {

    @Id
    private String id;
    private Binary colorfulImage;
    private Binary grayImage;
    private String contentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Binary getColorfulImage() {
        return colorfulImage;
    }

    public void setColorfulImage(Binary colorfulImage) {
        this.colorfulImage = colorfulImage;
    }

    public Binary getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(Binary grayImage) {
        this.grayImage = grayImage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
