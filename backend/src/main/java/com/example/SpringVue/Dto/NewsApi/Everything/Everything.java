package com.example.SpringVue.Dto.NewsApi.Everything;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Everything implements Serializable {

    private String status;

    private String code;

    private String message;

    private int totalResults;

    private List<Article> articles;

}
