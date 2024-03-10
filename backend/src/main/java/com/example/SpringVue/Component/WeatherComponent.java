package com.example.SpringVue.Component;

import com.example.SpringVue.Dto.WeatherApi.Search.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class WeatherComponent {

    private final String baseUrl;

    private final String key;

    private final RestTemplate restTemplate;

    public WeatherComponent(@Value("${utility-apis.weather-api-base-url}") String baseUrl, @Value("${utility-apis.weather-api-key}") String key, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.key = key;
        this.restTemplate = restTemplate;
    }

    public List<City> search(String query) {

        String requestUrl = baseUrl + "search.json?q=" + query + "&key=" + key;

        List<City> cities = restTemplate.getForObject(requestUrl, List.class);

        return cities;

    }
}
