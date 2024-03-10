package com.example.SpringVue.Controller;

import com.example.SpringVue.Service.WeatherService;
import com.example.SpringVue.Dto.WeatherApi.Search.City;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities(@RequestParam String query) {

        List<City> cities = weatherService.getCities(query);

        return new ResponseEntity<>(cities, HttpStatus.OK);

    }

}
