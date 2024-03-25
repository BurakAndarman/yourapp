package com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast;

import com.example.SpringVue.Dto.WeatherApi.Forecast.Condition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Day implements Serializable {

    private double avgtemp_c;

    private Condition condition;

}
