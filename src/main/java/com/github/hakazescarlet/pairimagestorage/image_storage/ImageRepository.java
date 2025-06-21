package com.github.hakazescarlet.pairimagestorage.image_storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {

    private static final String IMAGE_DB = "image_db";
    private static final String IMAGE_COLLECTION = "image_collection";

    private final MongoClient mongoClient;


    public ImageRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void save(Image colorfulImage, Image grayImage) {
        MongoCollection<Image> collection = mongoClient.getDatabase(IMAGE_DB)
                .getCollection(IMAGE_COLLECTION, Image.class);

        Document pairImageDocument = new Document()
                .append(colorfulImage.getName(), colorfulImage.getContent())
                .append(grayImage.getName(), grayImage.getContent());

        collection.insertOne(pairImageDocument);
    }
}
