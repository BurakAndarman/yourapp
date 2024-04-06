package com.example.SpringVue.Dto.WeatherApi.Forecast.Current;

import com.example.SpringVue.Dto.WeatherApi.Forecast.Condition;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current implements Serializable {

    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("temp_c")
    private double tempC;

    private Condition condition;

    @JsonProperty("wind_kph")
    private double windKph;

    @JsonProperty("wind_dir")
    private String windDir;

    private double humidity;

    @JsonProperty("feelslike_c")
    private double feelslikeC;

    private double uv;

}
