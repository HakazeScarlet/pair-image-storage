package com.github.hakazescarlet.pairimagestorage.pair_image;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PairImageRepository extends MongoRepository<PairImage, String> {

}
