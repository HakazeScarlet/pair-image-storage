package com.github.hakazescarlet.pairimagestorage.image_storage;

public class GrayImage {

    private String grayImageName;
    private byte[] grayImage;

    public GrayImage(String grayImageName, byte[] grayImage) {
        this.grayImageName = grayImageName;
        this.grayImage = grayImage;
    }

    public String getGrayImageName() {
        return grayImageName;
    }

    public void setGrayImageName(String grayImageName) {
        this.grayImageName = grayImageName;
    }

    public byte[] getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(byte[] grayImage) {
        this.grayImage = grayImage;
    }
}
