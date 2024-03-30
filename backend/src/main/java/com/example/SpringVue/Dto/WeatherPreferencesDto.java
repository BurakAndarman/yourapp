package com.example.SpringVue.Dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WeatherPreferencesDto {

    private String format;

    private String look;

    private List<WeatherPreferencesCitiesDto> cities;

}
