package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.Everything.Article;
import com.example.SpringVue.Dto.NewsApi.Everything.Everything;
import com.example.SpringVue.Request.SaveUserRequest;
import com.example.SpringVue.Service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

}