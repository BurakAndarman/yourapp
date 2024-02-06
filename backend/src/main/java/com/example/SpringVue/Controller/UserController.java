package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public UserController(UserService userService,ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {

        String userName = userService.addUser(userDto);

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/get-news")
    public ResponseEntity<HashMap<String,List<Article>>> getNews(Authentication authentication) {

        HashMap<String,List<Article>> userArticles = userService.getUserNews(authentication.getName());

        return new ResponseEntity<>(userArticles, HttpStatus.OK);
    }

    @GetMapping("/news-preferences")
    public ResponseEntity<NewsPreferencesDto> getNewsPreferences(Authentication authentication) {

        NewsPreferencesDto newsPreferencesDto = userService.getNewsPreferences(authentication.getName());

        return new ResponseEntity<>(newsPreferencesDto, HttpStatus.OK);

    }

    @PutMapping("/news-preferences")
    public ResponseEntity<String> updateNewsPreferences(@RequestBody NewsPreferencesDto newsPreferencesDto, Authentication authentication) {

        String updateMessage = userService.updateNewsPreferences(newsPreferencesDto, authentication.getName());

        return new ResponseEntity<>(updateMessage, HttpStatus.OK);

    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlansDto>> getPlans(Authentication authentication) {

        List<PlansDto> plansDtoList = userService.getPlans(authentication.getName());

        return new ResponseEntity<>(plansDtoList, HttpStatus.OK);

    }

    @PostMapping(value = "/plans",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> savePlans(@RequestParam("plansDtoList") String plansDtoList, @RequestParam("images") List<MultipartFile> images, Authentication authentication) throws IOException {


        images.stream().forEach(multipartFile -> {
            System.out.println(multipartFile.getOriginalFilename());
        });
        List<PlansDto> plans = objectMapper.readValue(plansDtoList,new TypeReference<>() { });

        System.out.println(plans.get(0).toString());

        return new ResponseEntity<>("heyyy!", HttpStatus.OK);

        /*
        String saveMessage = userService.savePlans(plansDtoList, authentication.getName());

        return new ResponseEntity<>(saveMessage, HttpStatus.OK);
*/
    }

}