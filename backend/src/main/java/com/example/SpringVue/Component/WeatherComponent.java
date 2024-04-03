package com.example.SpringVue.Component;

import com.example.SpringVue.Dto.WeatherApi.Forecast.ForecastWrapper;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import com.example.SpringVue.Service.Impl.NewsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class WeatherComponent {

    private static Logger log = LoggerFactory.getLogger(WeatherComponent.class);

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

        List<City> cities = Arrays.asList(restTemplate.getForEntity(requestUrl, City[].class).getBody());

        return cities;

    }

    public City search(double latitude, double longitude) {

        String requestUrl = baseUrl + "search.json?q=" + latitude + "," + longitude + "&key=" + key;

        City city = Arrays.asList(restTemplate.getForEntity(requestUrl, City[].class).getBody()).stream()
                .findFirst()
                .orElse(null);

        return city;

    }


    @Cacheable(value = "forecastCache", key = "#cityId")
    public ForecastWrapper forecast(int cityId) {

        log.info("Trying to fetch forecasts for city with city id " + cityId);

        String requestUrl = baseUrl + "forecast.json?q=id:" + cityId + "&days=5&aqi=no&alerts=no&key=" + key;

        ForecastWrapper forecastWrapper = restTemplate.getForObject(requestUrl, ForecastWrapper.class);

        return forecastWrapper;

    }

}
