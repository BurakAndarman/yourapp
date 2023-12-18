package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.Request.NewsPreferencesRequest;
import com.example.SpringVue.Dto.Request.SaveUserRequest;
import com.example.SpringVue.Dto.Response.NewsPreferencesResponse;

import java.util.List;

public interface UserService {

    String addUser(SaveUserRequest saveUserRequest);

    List<Article> getUserNews(String userName);

    NewsPreferencesResponse getNewsPreferences(String userName);

    String saveNewsPreferences(NewsPreferencesRequest newsPreferencesRequest, String userName);

}
