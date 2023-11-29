package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;

import java.util.HashMap;

public interface NewsService {

    TopHeadlines getTopHeadlines(HashMap<String, String> parameters);

}
