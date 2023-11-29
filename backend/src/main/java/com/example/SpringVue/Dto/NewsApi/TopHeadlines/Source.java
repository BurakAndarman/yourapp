package com.example.SpringVue.Dto.NewsApi.TopHeadlines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source implements Serializable {

    private String id;

    private String name;

}
