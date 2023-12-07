package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Service.NewsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class NewsServiceImpl implements NewsService {

    private final String baseUrl;
    private final String key;
    private final RestTemplate restTemplate;

    public NewsServiceImpl(@Value("${utility-apis.news-api-base-url}") String baseUrl,@Value("${utility-apis.news-api-key}") String key, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.key = key;
        this.restTemplate = restTemplate;
    }

    @Override
    public TopHeadlines getTopHeadlines(String category, String language) {

        String requestUrl = baseUrl + "top-headlines?category=" + category + "&language=" + language +  "&sortBy=popularity&apiKey=" + key;

        TopHeadlines topHeadlines = restTemplate.getForObject(requestUrl, TopHeadlines.class);

        return topHeadlines;
    }

    @Override
    public TopHeadlines getTopHeadlines(String language) {

        String requestUrl = baseUrl + "top-headlines?language=" + language +  "&sortBy=popularity&apiKey=" + key;

        TopHeadlines topHeadlines = restTemplate.getForObject(requestUrl, TopHeadlines.class);

        return topHeadlines;
    }

}
