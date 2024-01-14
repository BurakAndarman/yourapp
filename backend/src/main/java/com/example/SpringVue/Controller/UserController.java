package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/plans")
    public ResponseEntity<String> savePlans(@RequestBody List<PlansDto> plansDtoList, Authentication authentication) {

        String saveMessage = userService.savePlans(plansDtoList, authentication.getName());

        return new ResponseEntity<>(saveMessage, HttpStatus.OK);

    }

}