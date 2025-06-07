package com.github.hakazescarlet.pairimagestorage.image_storage;

import com.mongodb.client.MongoClient;
import org.springframework.stereotype.Repository;

@Repository
public class PairImageRepository {

    private static final String DB_NAME = "image_db";
    private static final String DB_COLLECTION = "image_db";

    private final MongoClient mongoClient;

    public PairImageRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

//    public void save(ColorImage colorImage, GrayImage grayImage) {
//        MongoCollection collection = mongoClient.getDatabase(DB_NAME)
//                .getCollection(DB_COLLECTION, )
//    }
}
