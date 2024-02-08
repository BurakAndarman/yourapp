package com.example.SpringVue.Service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface MediaService {

    HashMap<String, String> uploadFile(MultipartFile file, String folderName) throws IOException;

    void deleteFile(String publicId) throws IOException;

}
