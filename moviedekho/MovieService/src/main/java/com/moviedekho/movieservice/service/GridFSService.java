package com.moviedekho.movieservice.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GridFSService {

    @Autowired
    private GridFsOperations gridFsOperations;

    public String storeFile(MultipartFile file) throws IOException {
        // Store the file in GridFS and return the file ID
        ObjectId fileId = gridFsOperations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return fileId.toHexString();
    }

    public GridFsResource getFile(String fileId) {
        // Retrieve the file from GridFS using the file ID
        GridFSFile gridFSFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(new ObjectId(fileId))));
        return gridFsOperations.getResource(gridFSFile);
    }
}

