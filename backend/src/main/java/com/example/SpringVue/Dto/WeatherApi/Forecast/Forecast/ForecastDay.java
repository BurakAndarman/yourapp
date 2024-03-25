package com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast;

import com.example.SpringVue.Dto.WeatherApi.Forecast.Forecast.Day;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastDay implements Serializable {

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate date;

    private Day day;

}
