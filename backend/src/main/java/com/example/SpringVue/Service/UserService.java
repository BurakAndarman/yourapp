package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.Request.SaveUserRequest;

import java.util.List;

public interface UserService {

    String addUser(SaveUserRequest saveUserRequest);

    List<Article> getUserNews(String userName);

}
