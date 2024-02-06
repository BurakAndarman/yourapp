package com.example.SpringVue.Service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {

    String uploadFile(MultipartFile file, String folderName) throws IOException;

    void deleteFile(String fileName, String folderName) throws IOException;

}
