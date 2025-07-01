package com.github.hakazescarlet.pairimagestorage.utils;

import org.springframework.http.MediaType;

import java.util.Map;

public class MediaTypeResolver {

    private static final Map<String, String> MEDIA_TYPES = Map.of(
            "gif", MediaType.IMAGE_GIF_VALUE,
            "jpeg", MediaType.IMAGE_JPEG_VALUE,
            "jpg", MediaType.IMAGE_JPEG_VALUE,
            "png", MediaType.IMAGE_PNG_VALUE,
            "webp", "image/webp",
            "svg", "image/svg+xml",
            "bmp", "image/bmp"
    );

    private static final String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_OCTET_STREAM_VALUE;

    private MediaTypeResolver() {
        // hide public constructor
    }

    public static String resolve(String extension) {
        if (extension == null || extension.isBlank()) {
            return DEFAULT_MEDIA_TYPE;
        }
        return MEDIA_TYPES.getOrDefault(extension.trim().toLowerCase(), DEFAULT_MEDIA_TYPE);
    }
}