package com.github.hakazescarlet.pairimagestorage.http_client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestMultipartBody {

    private byte[] bytes;
    private String boundary;

    private HttpRequestMultipartBody(byte[] bytes, String boundary) {
        this.bytes = bytes;
        this.boundary = boundary;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public String getContentType() {
        return "multipart/form-data; boundary=" + this.getBoundary();
    }

    public byte[] getBody() {
        return this.bytes;
    }

    public static class Builder {
        private List<MultiPartRecord> parts;

        public Builder() {
            this.parts = new ArrayList<>();
        }

        public static class MultiPartRecord {
            private String fieldName;
            private String filename;
            private String contentType;
            private Object content;

            public String getFieldName() {
                return fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }

        }

        public Builder addPart(String fieldName, Object fieldValue, String contentType, String fileName) {
            MultiPartRecord part = new MultiPartRecord();
            part.setFieldName(fieldName);
            part.setContent(fieldValue);
            part.setContentType(contentType);
            part.setFilename(fileName);
            this.parts.add(part);
            return this;
        }

        public HttpRequestMultipartBody build() throws IOException {
            String boundary = new BigInteger(256, new SecureRandom()).toString();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (MultiPartRecord record : parts) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"" + record.getFieldName());

                if (record.getFilename() != null) {
                    stringBuilder.append("\"; filename=\"" + record.getFilename());
                }
                out.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                out.write(("\"\r\n").getBytes(StandardCharsets.UTF_8));
                Object content = record.getContent();

                if (content instanceof File) {
                    out.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                    Files.copy(((File) content).toPath(), out);
                } else {
                    out.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                    objectOutputStream.writeObject(content);
                    objectOutputStream.flush();
                }
                out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

            HttpRequestMultipartBody httpRequestMultipartBody = new HttpRequestMultipartBody(out.toByteArray(), boundary);
            return httpRequestMultipartBody;
        }

    }
}