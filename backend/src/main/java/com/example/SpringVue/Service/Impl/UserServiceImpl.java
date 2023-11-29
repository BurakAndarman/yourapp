package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Request.SaveUserRequest;
import com.example.SpringVue.Service.NewsService;
import com.example.SpringVue.Service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsManager userDetailsManager;

    private final NewsService newsService;

    public UserServiceImpl(UserDetailsManager userDetailsManager, NewsService newsService){
        this.userDetailsManager = userDetailsManager;
        this.newsService = newsService;
    }

    @Override
    public String addUser(SaveUserRequest saveUserRequest) {

        UserDetails user = User.withDefaultPasswordEncoder()
                                .username(saveUserRequest.getUserName())
                                .password(saveUserRequest.getPassword())
                                .authorities("REGULAR") // There is only one user type for now
                                .build();

        userDetailsManager.createUser(user);

        return user.getUsername();
    }

    @Override
    public TopHeadlines getUserNews(String userName) {
        HashMap<String, String> userDefinedParameters = new HashMap<String, String>();
        userDefinedParameters.put("category","technology");

        // TODO: It will take user preferred category
        TopHeadlines userTopHeadlines = newsService.getTopHeadlines(userDefinedParameters);

        return userTopHeadlines;

    }


}
