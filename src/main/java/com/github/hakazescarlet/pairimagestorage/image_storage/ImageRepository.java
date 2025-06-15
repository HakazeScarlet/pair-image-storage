package com.github.hakazescarlet.pairimagestorage.image_storage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<PairImage, String> {

}
