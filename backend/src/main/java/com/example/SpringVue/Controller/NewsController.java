package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<HashMap<String, List<Article>>> getUserNews(Authentication authentication) {

        HashMap<String,List<Article>> userArticles = newsService.getUserNews(authentication.getName());

        return new ResponseEntity<>(userArticles, HttpStatus.OK);
    }

    @GetMapping("/news-preferences")
    public ResponseEntity<NewsPreferencesDto> getNewsPreferences(Authentication authentication) {

        NewsPreferencesDto newsPreferencesDto = newsService.getNewsPreferences(authentication.getName());

        return new ResponseEntity<>(newsPreferencesDto, HttpStatus.OK);

    }

    @PutMapping("/news-preferences")
    public ResponseEntity<String> updateNewsPreferences(@RequestBody NewsPreferencesDto newsPreferencesDto, Authentication authentication) {

        String updateMessage = newsService.updateNewsPreferences(newsPreferencesDto, authentication.getName());

        return new ResponseEntity<>(updateMessage, HttpStatus.OK);

    }

}
