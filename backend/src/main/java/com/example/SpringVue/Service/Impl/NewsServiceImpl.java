package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.Everything.Everything;
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

    public Everything getEverything(String q, String language) {

        String requestUrl = baseUrl + "top-headlines?q=" + q + "&language=" + language +  "&sortBy=popularity&apiKey=" + key;

        Everything everything = restTemplate.getForObject(requestUrl, Everything.class);

        return everything;
    }

    @Override
    public Everything getEverything(String language) {

        String requestUrl = baseUrl + "top-headlines?language=" + language +  "&sortBy=popularity&apiKey=" + key;

        Everything everything = restTemplate.getForObject(requestUrl, Everything.class);

        return everything;
    }

}
