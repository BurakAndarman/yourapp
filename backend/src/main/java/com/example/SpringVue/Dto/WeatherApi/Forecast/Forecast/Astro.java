package com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Astro implements Serializable {

    private String sunrise;

    private String sunset;

}
