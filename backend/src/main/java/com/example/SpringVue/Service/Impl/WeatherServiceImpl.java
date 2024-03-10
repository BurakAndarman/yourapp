package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Component.WeatherComponent;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import com.example.SpringVue.Repo.WeatherPreferencesCitiesRepository;
import com.example.SpringVue.Repo.WeatherPreferencesRepository;
import com.example.SpringVue.Service.UserService;
import com.example.SpringVue.Service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherPreferencesRepository weatherPreferencesRepository;

    private final WeatherPreferencesCitiesRepository weatherPreferencesCitiesRepository;

    private final UserService userService;

    private final WeatherComponent weatherComponent;

    public WeatherServiceImpl(WeatherPreferencesRepository weatherPreferencesRepository, WeatherPreferencesCitiesRepository weatherPreferencesCitiesRepository,
                              UserService userService, WeatherComponent weatherComponent) {
        this.weatherPreferencesRepository = weatherPreferencesRepository;
        this.weatherPreferencesCitiesRepository = weatherPreferencesCitiesRepository;
        this.userService = userService;
        this.weatherComponent = weatherComponent;
    }

    @Override
    public List<City> getCities(String query) {
        return weatherComponent.search(query);
    }

}
