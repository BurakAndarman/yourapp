package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Entity.User;

import java.util.HashMap;
import java.util.List;

public interface NewsService {

    HashMap<String, List<Article>> getUserNews(String userName);

    void saveNewsPreferences(String userName, User user);

    NewsPreferencesDto getNewsPreferences(String userName);

    String updateNewsPreferences(NewsPreferencesDto newsPreferencesDto, String userName);

}
