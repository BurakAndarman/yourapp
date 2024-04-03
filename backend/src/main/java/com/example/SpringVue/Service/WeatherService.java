package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.ForecastsDto;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import com.example.SpringVue.Dto.WeatherPreferencesDto;

import java.util.List;

public interface WeatherService {

    ForecastsDto getForecasts(double latitude, double longitude, String userName);

    List<City> getCities(String query);

    WeatherPreferencesDto getWeatherPreferences(String userName);

    String updateWeatherPreferences(WeatherPreferencesDto weatherPreferencesDto, String userName);

}
