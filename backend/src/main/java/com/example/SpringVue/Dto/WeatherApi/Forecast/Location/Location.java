package com.example.SpringVue.Dto.WeatherApi.Forecast.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {

    private String name;

    private String region;

    private String country;

    private String localtime;

}
