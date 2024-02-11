package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Dto.NewsPreferencesDto;
import com.example.SpringVue.Entity.NewsPreferences;
import com.example.SpringVue.Entity.User;
import com.example.SpringVue.Exception.NewsPreferenceNotFound;
import com.example.SpringVue.Repo.NewsPreferencesRepository;
import com.example.SpringVue.Service.NewsService;
import com.example.SpringVue.Service.UserNewsService;
import com.example.SpringVue.Utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserNewsServiceImpl implements UserNewsService {

    private static Logger log = LoggerFactory.getLogger(UserNewsServiceImpl.class);

    private final NewsPreferencesRepository newsPreferencesRepository;

    private final NewsService newsService;

    private final CacheUtils cacheUtils;

    public UserNewsServiceImpl(NewsPreferencesRepository newsPreferencesRepository,NewsService newsService,CacheUtils cacheUtils) {
        this.newsService = newsService;
        this.newsPreferencesRepository = newsPreferencesRepository;
        this.cacheUtils = cacheUtils;
    }

    @Cacheable(value = "userNewsCache", key = "#userName")
    @Override
    public HashMap<String,List<Article>> getUserNews(String userName) {

        log.info("Trying to fetch data from 3rd party api");

        Optional<NewsPreferences> newsPreferences = newsPreferencesRepository.findById(userName);

        if(newsPreferences.isEmpty()) {
            throw new NewsPreferenceNotFound("Couldn't find any user preference",userName);
        }

        NewsPreferences validatedNewsPreferences = newsPreferences.get();

        String preferredLanguage = validatedNewsPreferences.getLanguage();
        Boolean topicsEmpty = validatedNewsPreferences.getInterestedTopics().isEmpty();

        HashMap<String,List<Article>> articlesMap = new HashMap<>();

        if(topicsEmpty) {
            TopHeadlines topHeadlines = newsService.getTopHeadlines(preferredLanguage);

            articlesMap.put("general",topHeadlines.getArticles().stream().limit(12).toList());

            return articlesMap;
        }

        List<String> preferredTopics = Arrays.stream(validatedNewsPreferences.getInterestedTopics().split(",")).toList(); // Splitting comma separated topics

        preferredTopics.stream().forEach(preferredTopic -> {
            TopHeadlines topHeadlines = newsService.getTopHeadlines(preferredTopic,preferredLanguage);

            articlesMap.put(preferredTopic,topHeadlines.getArticles().stream().limit(6).toList());
        });

        return articlesMap;
    }

    @Override
    public void saveNewsPreferences(String userName, User user) {
        newsPreferencesRepository.save(new NewsPreferences(userName,user));
    }

    @Override
    public NewsPreferencesDto getNewsPreferences(String userName) {

        Optional<NewsPreferences> newsPreferences = newsPreferencesRepository.findById(userName);

        if(newsPreferences.isEmpty()) {
            throw new NewsPreferenceNotFound("Couldn't find any user preference",userName);
        }

        String language = newsPreferences.get().getLanguage();
        List<String> interestedTopics = new ArrayList<>();

        if(!newsPreferences.get().getInterestedTopics().isEmpty()) {
            interestedTopics.addAll(Arrays.stream(newsPreferences.get().getInterestedTopics().split(",")).toList());
        }

        return new NewsPreferencesDto(language, interestedTopics);
    }

    @Override
    public String updateNewsPreferences(NewsPreferencesDto newsPreferencesDto, String userName) {

        String language = newsPreferencesDto.getLanguage();
        String interestedTopics = "";

        if(!newsPreferencesDto.getInterestedTopics().isEmpty()) {
            interestedTopics += String.join(",", newsPreferencesDto.getInterestedTopics());
        }

        NewsPreferences newsPreferences = new NewsPreferences(
                userName,
                language,
                interestedTopics
        );

        newsPreferencesRepository.save(newsPreferences);

        cacheUtils.evictUserNews(userName);

        return "Changes recorded successfully";

    }

}
