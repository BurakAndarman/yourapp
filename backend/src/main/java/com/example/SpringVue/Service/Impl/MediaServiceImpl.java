package com.example.SpringVue.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    public void deleteFile(String publicId) throws IOException {

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

    }

    @Override
    public HashMap<String,String> uploadFile(MultipartFile file, String folderName) throws IOException {

        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);

        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
        String publicId = uploadedFile.get("public_id").toString();

        HashMap<String,String> uploadResponse = new HashMap<>();
        uploadResponse.put("public_id",publicId);
        uploadResponse.put("image_url",cloudinary.url().secure(true).generate(publicId));

        return uploadResponse;
    }
}
