package com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast implements Serializable {

    private List<ForecastDay> forecastday;

}
