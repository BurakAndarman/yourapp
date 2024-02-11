package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.PlansDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlansService {

    List<PlansDto> getPlans(String userName);

    String savePlans(List<PlansDto> plansDtoList, List<MultipartFile> images, String userName) throws IOException;

}
