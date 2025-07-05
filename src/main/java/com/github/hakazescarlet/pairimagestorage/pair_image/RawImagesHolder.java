package com.github.hakazescarlet.pairimagestorage.pair_image;

import java.util.Arrays;
import java.util.Objects;

public class RawImagesHolder {

    private byte[] colorful;
    private byte[] gray;
    private String contentType;

    public RawImagesHolder(byte[] colorful, byte[] gray, String contentType) {
        this.colorful = colorful;
        this.gray = gray;
        this.contentType = contentType;
    }

    public byte[] getColorful() {
        return colorful;
    }

    public void setColorful(byte[] colorful) {
        this.colorful = colorful;
    }

    public byte[] getGray() {
        return gray;
    }

    public void setGray(byte[] gray) {
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
        RawImagesHolder that = (RawImagesHolder) o;
        return Objects.deepEquals(colorful, that.colorful) && Objects.deepEquals(gray, that.gray) && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(colorful), Arrays.hashCode(gray), contentType);
    }
}
