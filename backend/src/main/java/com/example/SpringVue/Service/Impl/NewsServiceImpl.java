package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Service.NewsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

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

    // TODO: Keep the parameters in a hashmap
    public TopHeadlines getTopHeadlines(HashMap<String,String> parameters) {

        String requestUrl = baseUrl + "top-headlines?category=" + parameters.get("category") + "&apiKey=" + key;
        TopHeadlines topHeadlines = restTemplate.getForObject(requestUrl, TopHeadlines.class);

        return topHeadlines;
    }

}
