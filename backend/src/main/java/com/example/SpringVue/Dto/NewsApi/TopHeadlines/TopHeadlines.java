package com.example.SpringVue.Dto.NewsApi.TopHeadlines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopHeadlines implements Serializable {

    private String status;

    private int totalResults;

    private List<Article> articles;

}
