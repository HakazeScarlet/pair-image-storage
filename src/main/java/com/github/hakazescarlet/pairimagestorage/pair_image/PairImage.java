package com.github.hakazescarlet.pairimagestorage.pair_image;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class PairImage {

    @Id
    private String id;
    private Binary colorful;
    private Binary gray;
    private String contentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Binary getColorful() {
        return colorful;
    }

    public void setColorful(Binary colorful) {
        this.colorful = colorful;
    }

    public Binary getGray() {
        return gray;
    }

    public void setGray(Binary gray) {
        this.gray = gray;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PairImage pairImage = (PairImage) o;
        return Objects.equals(id, pairImage.id) && Objects.equals(colorful, pairImage.colorful) && Objects.equals(gray, pairImage.gray) && Objects.equals(contentType, pairImage.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, colorful, gray, contentType);
    }

    @Override
    public String toString() {
        return "PairImage{" +
            "id='" + id + '\'' +
            ", colorful=" + colorful +
            ", gray=" + gray +
            ", contentType='" + contentType + '\'' +
            '}';
    }
}
