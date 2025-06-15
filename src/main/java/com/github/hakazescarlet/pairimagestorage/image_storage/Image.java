package com.github.hakazescarlet.pairimagestorage.image_storage;

public class Image {

    private String imageName;
    private byte[] content;

    public Image(String imageName, byte[] content) {
        this.imageName = imageName;
        this.content = content;
    }

    public String getName() {
        return imageName;
    }

    public void setName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
