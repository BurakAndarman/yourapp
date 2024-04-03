package com.example.SpringVue.Dto.WeatherApi.Search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class City implements Serializable {

    private int id;

    private String name;

    private String region;

    private String country;

}
