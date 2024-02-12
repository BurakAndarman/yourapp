package com.example.SpringVue.Components;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NewsComponent {

    private final String baseUrl;
    private final String key;
    private final RestTemplate restTemplate;

    public NewsComponent(@Value("${utility-apis.news-api-base-url}") String baseUrl, @Value("${utility-apis.news-api-key}") String key, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.key = key;
        this.restTemplate = restTemplate;
    }

    public TopHeadlines getTopHeadlines(String category, String language) {

        String requestUrl = baseUrl + "top-headlines?category=" + category + "&language=" + language +  "&sortBy=popularity&apiKey=" + key;

        TopHeadlines topHeadlines = restTemplate.getForObject(requestUrl, TopHeadlines.class);

        return topHeadlines;
    }

    public TopHeadlines getTopHeadlines(String language) {

        String requestUrl = baseUrl + "top-headlines?language=" + language +  "&sortBy=popularity&apiKey=" + key;

        TopHeadlines topHeadlines = restTemplate.getForObject(requestUrl, TopHeadlines.class);

        return topHeadlines;
    }

}
