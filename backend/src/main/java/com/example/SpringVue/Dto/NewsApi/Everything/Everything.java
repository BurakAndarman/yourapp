package com.example.SpringVue.Dto.NewsApi.Everything;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Everything implements Serializable {

    private String status;

    private String code; // For falsy responses

    private String message; // For falsy responses

    private int totalResults;

    private List<Article> articles;

}
