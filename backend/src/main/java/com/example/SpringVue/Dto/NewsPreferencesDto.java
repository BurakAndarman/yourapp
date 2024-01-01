package com.example.SpringVue.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class NewsPreferencesDto {

    private String language;

    private List<String> interestedTopics;

}
