package com.example.SpringVue.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MediaComponent {

    private final Cloudinary cloudinary;

    public MediaComponent(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    
    public void deleteFile(String publicId) throws IOException {

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

    }
    
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
