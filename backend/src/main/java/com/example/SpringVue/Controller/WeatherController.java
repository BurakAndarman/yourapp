package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.ForecastsDto;
import com.example.SpringVue.Dto.WeatherPreferencesDto;
import com.example.SpringVue.Service.WeatherService;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<ForecastsDto> getForecasts(@RequestParam double latitude, @RequestParam double longitude, Authentication authentication) {

        ForecastsDto forecastsDto = weatherService.getForecasts(latitude, longitude, authentication.getName());

        return new ResponseEntity<>(forecastsDto, HttpStatus.OK);

    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities(@RequestParam String query) {

        List<City> cities = weatherService.getCities(query);

        return new ResponseEntity<>(cities, HttpStatus.OK);

    }

    @GetMapping("/weather-preferences")
    public ResponseEntity<WeatherPreferencesDto> getWeatherPreferences(Authentication authentication) {

        WeatherPreferencesDto weatherPreferencesDto = weatherService.getWeatherPreferences(authentication.getName());

        return new ResponseEntity<>(weatherPreferencesDto, HttpStatus.OK);

    }

    @PutMapping("/weather-preferences")
    public ResponseEntity<String> updateWeatherPreferences(@RequestBody WeatherPreferencesDto weatherPreferencesDto, Authentication authentication) {

        String updateMessage = weatherService.updateWeatherPreferences(weatherPreferencesDto, authentication.getName());

        return new ResponseEntity<>(updateMessage, HttpStatus.OK);

    }

}
