package com.example.SpringVue.Service.Impl;

import com.cloudinary.Cloudinary;
import com.example.SpringVue.Service.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MediaServiceImpl implements MediaService {

    private final Cloudinary cloudinary;

    public MediaServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void deleteFile(String fileName, String folderName) throws IOException {

        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);
        cloudinary.uploader().destroy(fileName, options);
    }

    @Override
    public String uploadFile(MultipartFile file, String folderName) throws IOException {

        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);
        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
        String publicId = uploadedFile.get("public_id").toString();

        return cloudinary.url().secure(true).generate(publicId);
    }
}
