package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.Request.UpdateNewsPreferencesRequest;
import com.example.SpringVue.Dto.Request.SavePlansRequest;
import com.example.SpringVue.Dto.Request.SaveUserRequest;
import com.example.SpringVue.Dto.Response.GetNewsPreferencesResponse;
import com.example.SpringVue.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody SaveUserRequest saveUserRequest) {

        String userName = userService.addUser(saveUserRequest);

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/get-news")
    public ResponseEntity<List<Article>> getNews(Authentication authentication) {

        List<Article> userArticles = userService.getUserNews(authentication.getName());

        return new ResponseEntity<>(userArticles, HttpStatus.OK);
    }

    @GetMapping("/news-preferences")
    public ResponseEntity<GetNewsPreferencesResponse> getNewsPreferences(Authentication authentication) {

        GetNewsPreferencesResponse getNewsPreferencesResponse = userService.getNewsPreferences(authentication.getName());

        return new ResponseEntity<>(getNewsPreferencesResponse, HttpStatus.OK);

    }

    @PutMapping("/news-preferences")
    public ResponseEntity<String> updateNewsPreferences (@RequestBody UpdateNewsPreferencesRequest updateNewsPreferencesRequest, Authentication authentication) {

        String saveMessage = userService.updateNewsPreferences(updateNewsPreferencesRequest, authentication.getName());

        return new ResponseEntity<>(saveMessage, HttpStatus.OK);

    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlansDto>> getPlans(Authentication authentication) {

        List<PlansDto> plansDtoList = userService.getPlans(authentication.getName());

        return new ResponseEntity<>(plansDtoList, HttpStatus.OK);

    }

    @PostMapping("/plans")
    public ResponseEntity<String> savePlans(@RequestBody SavePlansRequest savePlansRequest, Authentication authentication) {

        String saveMessage = userService.savePlans(savePlansRequest, authentication.getName());

        return new ResponseEntity<>(saveMessage, HttpStatus.CREATED);
    }


}