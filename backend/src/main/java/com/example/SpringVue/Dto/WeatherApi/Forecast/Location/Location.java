package com.example.SpringVue.Dto.WeatherApi.Forecast.Location;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {

    private String name;

    private String region;

    private String country;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime localtime;

}
