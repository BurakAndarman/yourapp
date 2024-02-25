package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Entity.NewsPreferences;
import com.example.SpringVue.Entity.User;

import java.util.HashMap;
import java.util.List;

public interface NewsService {

    HashMap<String, List<Article>> getUserNews(String userName);

    NewsPreferencesDto getNewsPreferences(String userName);

    String updateNewsPreferences(NewsPreferencesDto newsPreferencesDto, String userName);

}
