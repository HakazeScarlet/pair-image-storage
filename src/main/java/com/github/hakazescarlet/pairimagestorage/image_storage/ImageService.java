package com.github.hakazescarlet.pairimagestorage.image_storage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private ImageRepository imageRepository;
    private GridFsTemplate gridFsTemplate;

    public void savePair(PairImage pairImgBytesWrapper) {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "image");
        ObjectId id = gridFsTemplate.store(
                pairImgBytesWrapper, pairImgBytesWrapper.getPairName()
        )
        MongoConnectionDetails.GridFs gfsPhoto = new GridFS(db, "photo");
        GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
        gfsFile.setFilename(newFileName);
        gfsFile.save();
    }
}
