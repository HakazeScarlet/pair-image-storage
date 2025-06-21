package com.github.hakazescarlet.pairimagestorage.image_storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class Image {

    @Id
    private String id;

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
