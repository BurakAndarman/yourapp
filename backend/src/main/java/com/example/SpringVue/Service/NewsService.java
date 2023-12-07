package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;

public interface NewsService {

    TopHeadlines getTopHeadlines(String category, String language);

    TopHeadlines getTopHeadlines(String language);

}
