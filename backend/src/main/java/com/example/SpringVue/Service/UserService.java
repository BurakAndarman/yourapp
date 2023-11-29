package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Request.SaveUserRequest;

public interface UserService {

    String addUser(SaveUserRequest saveUserRequest);

    TopHeadlines getUserNews(String userName);

}
