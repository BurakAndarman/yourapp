package com.example.SpringVue.Dto;

import com.example.SpringVue.Dto.WeatherApi.Forecast.ForecastWrapper;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ForecastsDto {

    List<ForecastWrapper> forecastsList;

    String format;

    String look;

}
