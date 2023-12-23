package com.example.SpringVue.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class SaveNewsPreferencesRequest {

    private String language;

    private List<String> interestedTopics;

}
