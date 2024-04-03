package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Component.WeatherComponent;
import com.example.SpringVue.Dto.ForecastsDto;
import com.example.SpringVue.Dto.WeatherApi.Forecast.ForecastWrapper;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import com.example.SpringVue.Dto.WeatherPreferencesCitiesDto;
import com.example.SpringVue.Dto.WeatherPreferencesDto;
import com.example.SpringVue.Entity.User;
import com.example.SpringVue.Entity.WeatherPreferences;
import com.example.SpringVue.Entity.WeatherPreferencesCities;
import com.example.SpringVue.Repo.WeatherPreferencesRepository;
import com.example.SpringVue.Service.UserService;
import com.example.SpringVue.Service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherPreferencesRepository weatherPreferencesRepository;

    private final UserService userService;

    private final WeatherComponent weatherComponent;

    public WeatherServiceImpl(WeatherPreferencesRepository weatherPreferencesRepository,
                              UserService userService, WeatherComponent weatherComponent) {
        this.weatherPreferencesRepository = weatherPreferencesRepository;
        this.userService = userService;
        this.weatherComponent = weatherComponent;
    }

    @Override
    public ForecastsDto getForecasts(double latitude, double longitude, String userName) {

        City userLocation = weatherComponent.search(latitude, longitude);

        Optional<WeatherPreferences> weatherPreferences = weatherPreferencesRepository.findById(userName);

        if(weatherPreferences.isEmpty()) {

            if(userLocation == null) {
                throw new RuntimeException("Couldn't find user location");
            }

            User user = userService.getUser(userName);

            WeatherPreferences userWeatherPreferences = new WeatherPreferences(
                    userName,
                    user
            );

            WeatherPreferencesCities userCity = new WeatherPreferencesCities(
                userWeatherPreferences,
                userLocation.getId(),
                userLocation.getName(),
                0
            );

            userWeatherPreferences.addCity(userCity);

            userWeatherPreferences = weatherPreferencesRepository.save(userWeatherPreferences);

            return this.createForecastsResponse(userWeatherPreferences);

        }

        WeatherPreferences userWeatherPreferences = weatherPreferences.get();

        if(userLocation != null) {
            WeatherPreferencesCities newUserLocationCity = userWeatherPreferences.getFirstCityByCityId(userLocation.getId());

            if(newUserLocationCity == null) {
                WeatherPreferencesCities userCity = userWeatherPreferences.getFirstCityByOrderNo(0);

                userCity.setCityId(userLocation.getId());
                userCity.setName(userLocation.getName());

                userWeatherPreferences = weatherPreferencesRepository.save(userWeatherPreferences);

            } else if (newUserLocationCity.getOrderNo() != 0) {
                WeatherPreferencesCities userCity = userWeatherPreferences.getFirstCityByOrderNo(0);

                userCity.setOrderNo(newUserLocationCity.getOrderNo());
                newUserLocationCity.setOrderNo(0);

                userWeatherPreferences = weatherPreferencesRepository.save(userWeatherPreferences);

            }

        }

        return this.createForecastsResponse(userWeatherPreferences);

    }

    private ForecastsDto createForecastsResponse(WeatherPreferences weatherPreferences) {
        List<ForecastWrapper> forecastsList = new ArrayList<>();

        weatherPreferences.getWeatherPreferencesCities().stream().forEach(weatherPreferencesCities -> forecastsList.add(weatherComponent.forecast(weatherPreferencesCities.getCityId())));

        return new ForecastsDto(forecastsList,weatherPreferences.getFormat(),weatherPreferences.getLook());
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

            WeatherPreferencesCities weatherPreferencesCity = userWeatherPreferences.getFirstCityByCityId(weatherPreferencesCitiesDto.getCityId());

            if(weatherPreferencesCity == null) {
                userWeatherPreferences.addCity(new WeatherPreferencesCities(
                        userWeatherPreferences,
                        weatherPreferencesCitiesDto.getCityId(),
                        weatherPreferencesCitiesDto.getName(),
                        weatherPreferencesCitiesDto.getOrderNo()
                ));

            } else if(weatherPreferencesCitiesDto.getOrderNo() != weatherPreferencesCity.getOrderNo()){
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


}
