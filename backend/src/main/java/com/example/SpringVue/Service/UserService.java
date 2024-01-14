package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.UserDto;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    String addUser(UserDto userDto);

    HashMap<String,List<Article>> getUserNews(String userName);

    NewsPreferencesDto getNewsPreferences(String userName);

    String updateNewsPreferences(NewsPreferencesDto newsPreferencesDto, String userName);

    List<PlansDto> getPlans(String userName);

    String savePlans(List<PlansDto> plansDtoList, String userName);

}
