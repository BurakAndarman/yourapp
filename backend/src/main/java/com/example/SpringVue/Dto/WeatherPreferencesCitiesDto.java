package com.example.SpringVue.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WeatherPreferencesCitiesDto {

    private int cityId;

    private String name;

    private int orderNo;

}