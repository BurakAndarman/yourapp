package com.example.SpringVue.Dto.WeatherApi.Forecast;

import com.example.SpringVue.Dto.WeatherApi.Forecast.Current.Current;
import com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast.Forecast;
import com.example.SpringVue.Dto.WeatherApi.Forecast.Location.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastWrapper implements Serializable {

    private Location location;

    private Current current;

    private Forecast forecast;

}
