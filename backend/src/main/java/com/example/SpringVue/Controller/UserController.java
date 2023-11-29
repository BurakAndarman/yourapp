package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Request.SaveUserRequest;
import com.example.SpringVue.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public String saveUser(@RequestBody SaveUserRequest saveUserRequest) {

        String userName = userService.addUser(saveUserRequest);

        return userName;
    }

    @GetMapping("/get-news")
    public TopHeadlines saveUser(Authentication authentication) {

        TopHeadlines userTopHeadlines = userService.getUserNews(authentication.getName());

        return userTopHeadlines;
    }

}