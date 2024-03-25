package com.example.SpringVue.Dto.WeatherApi.Forecast.Current;

import com.example.SpringVue.Dto.WeatherApi.Forecast.Condition;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current implements Serializable {

    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime last_updated;

    private double temp_c;

    private Condition condition;

    private double wind_kph;

    private String wind_dir;

    private double humidity;

    private double feelslike_c;

    private double uv;

}
