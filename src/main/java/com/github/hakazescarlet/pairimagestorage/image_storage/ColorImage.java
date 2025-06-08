package com.github.hakazescarlet.pairimagestorage.image_storage;

public class ColorImage {

    private String imageName;
    private byte[] colorImage;

    public ColorImage(String imageName, byte[] colorImage) {
        this.imageName = imageName;
        this.colorImage = colorImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getColorImage() {
        return colorImage;
    }

    public void setColorImage(byte[] colorImage) {
        this.colorImage = colorImage;
    }
}
