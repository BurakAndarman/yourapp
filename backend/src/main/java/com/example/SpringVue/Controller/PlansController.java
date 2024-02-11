package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Service.PlansService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user/plans")
public class PlansController {

    private final PlansService plansService;

    private final ObjectMapper objectMapper;

    public PlansController(PlansService plansService,ObjectMapper objectMapper) {
        this.plansService = plansService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<PlansDto>> getPlans(Authentication authentication) {

        List<PlansDto> plansDtoList = plansService.getPlans(authentication.getName());

        return new ResponseEntity<>(plansDtoList, HttpStatus.OK);

    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> savePlans(@RequestParam("plansDtoList") String plansDtoList, @RequestParam(value = "images",required = false) List<MultipartFile> images, Authentication authentication) throws IOException {
        List<PlansDto> plans = objectMapper.readValue(plansDtoList,new TypeReference<>() { });

        String saveMessage = plansService.savePlans(plans, images, authentication.getName());

        return new ResponseEntity<>(saveMessage, HttpStatus.OK);
    }


}
