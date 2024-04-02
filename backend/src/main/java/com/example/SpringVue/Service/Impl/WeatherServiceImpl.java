package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Component.WeatherComponent;
import com.example.SpringVue.Dto.WeatherApi.Forecast.ForecastWrapper;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import com.example.SpringVue.Dto.WeatherPreferencesCitiesDto;
import com.example.SpringVue.Dto.WeatherPreferencesDto;
import com.example.SpringVue.Entity.WeatherPreferences;
import com.example.SpringVue.Entity.WeatherPreferencesCities;
import com.example.SpringVue.Repo.WeatherPreferencesCitiesRepository;
import com.example.SpringVue.Repo.WeatherPreferencesRepository;
import com.example.SpringVue.Service.UserService;
import com.example.SpringVue.Service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public WeatherPreferencesDto getWeatherPreferences(String userName) {

        Optional<WeatherPreferences> weatherPreferences = weatherPreferencesRepository.findById(userName);

        if(weatherPreferences.isEmpty()) {
            throw new RuntimeException("Couldn't find any user preference (Username that caused the error: "+userName+")");
        }

        WeatherPreferences userWeatherPreferences = weatherPreferences.get();

        String format = userWeatherPreferences.getFormat();
        String look = userWeatherPreferences.getLook();
        List<WeatherPreferencesCitiesDto> weatherPreferencesCitiesDtos = userWeatherPreferences.getWeatherPreferencesCities().stream().map(weatherPreferencesCities -> new WeatherPreferencesCitiesDto(
                weatherPreferencesCities.getCityId(),
                weatherPreferencesCities.getName(),
                weatherPreferencesCities.getOrderNo()
        )).toList();


        return new WeatherPreferencesDto(format, look, weatherPreferencesCitiesDtos);
    }

    @Override
    public String updateWeatherPreferences(WeatherPreferencesDto weatherPreferencesDto, String userName) {

        WeatherPreferences userWeatherPreferences = weatherPreferencesRepository.findById(userName).get();

        userWeatherPreferences.setFormat(weatherPreferencesDto.getFormat());
        userWeatherPreferences.setLook(weatherPreferencesDto.getLook());

        List<Integer> currentCityIds = new ArrayList<>();

        weatherPreferencesDto.getCities().stream().forEach(weatherPreferencesCitiesDto -> {
            currentCityIds.add(weatherPreferencesCitiesDto.getCityId());

            WeatherPreferencesCities weatherPreferencesCity = userWeatherPreferences.getWeatherPreferencesCities().stream()
                    .filter(weatherPreferencesCities -> weatherPreferencesCities.getCityId() == weatherPreferencesCitiesDto.getCityId())
                    .findAny()
                    .orElse(null);

            if(weatherPreferencesCity == null) {
                userWeatherPreferences.addCity(new WeatherPreferencesCities(
                        userWeatherPreferences,
                        weatherPreferencesCitiesDto.getCityId(),
                        weatherPreferencesCitiesDto.getName(),
                        weatherPreferencesCitiesDto.getOrderNo()
                ));

            } else {
                weatherPreferencesCity.setOrderNo(weatherPreferencesCitiesDto.getOrderNo());

            }
        });

        Set<WeatherPreferencesCities> weatherPreferencesCitiesToRemove = userWeatherPreferences.getWeatherPreferencesCities().stream()
                .filter(weatherPreferencesCities -> !currentCityIds.contains(weatherPreferencesCities.getCityId()))
                .collect(Collectors.toSet());

        userWeatherPreferences.removeCities(weatherPreferencesCitiesToRemove);

        weatherPreferencesRepository.save(userWeatherPreferences);

        return "Changes recorded successfully";
    }


    public ForecastWrapper getForecasts() {
        return null;
    }

}
