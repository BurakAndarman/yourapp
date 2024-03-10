package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.WeatherApi.Search.City;

import java.util.List;

public interface WeatherService {

    List<City> getCities(String query);

}
