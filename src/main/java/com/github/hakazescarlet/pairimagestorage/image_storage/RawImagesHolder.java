package com.github.hakazescarlet.pairimagestorage.image_storage;

public class RawImagesHolder {

    private byte[] colorfulImage;
    private byte[] grayImage;
    private String contentType;

    public RawImagesHolder(byte[] colorfulImage, byte[] grayImage, String contentType) {
        this.colorfulImage = colorfulImage;
        this.grayImage = grayImage;
        this.contentType = contentType;
    }

    public byte[] getColorfulImage() {
        return colorfulImage;
    }

    public void setColorfulImage(byte[] colorfulImage) {
        this.colorfulImage = colorfulImage;
    }

    public byte[] getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(byte[] grayImage) {
        this.grayImage = grayImage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
