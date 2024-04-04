package com.example.SpringVue.Dto.WeatherApi.Forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition implements Serializable {

    private String text;
    
    private int code;

}
