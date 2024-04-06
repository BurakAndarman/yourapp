package com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Astro {

    private String sunrise;

    private String sunset;

}
