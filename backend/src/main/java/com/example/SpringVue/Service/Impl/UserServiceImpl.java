package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.Everything.Article;
import com.example.SpringVue.Dto.NewsApi.Everything.Everything;
import com.example.SpringVue.Entity.NewsPreferences;
import com.example.SpringVue.Exception.NewsPreferenceNotFound;
import com.example.SpringVue.Repo.NewsPreferencesRepository;
import com.example.SpringVue.Request.SaveUserRequest;
import com.example.SpringVue.Service.NewsService;
import com.example.SpringVue.Service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsManager userDetailsManager;

    private final NewsService newsService;

    private final NewsPreferencesRepository newsPreferencesRepository;

    public UserServiceImpl(UserDetailsManager userDetailsManager, NewsService newsService, NewsPreferencesRepository newsPreferencesRepository){
        this.userDetailsManager = userDetailsManager;
        this.newsService = newsService;
        this.newsPreferencesRepository = newsPreferencesRepository;
    }

    @Override
    public String addUser(SaveUserRequest saveUserRequest) {

        UserDetails user = User.withDefaultPasswordEncoder()
                                .username(saveUserRequest.getUserName())
                                .password(saveUserRequest.getPassword())
                                .authorities("REGULAR") // There is only one type of user now
                                .build();

        newsPreferencesRepository.save(new NewsPreferences(user.getUsername()));

        userDetailsManager.createUser(user);

        return user.getUsername();
    }

    @Override
    public List<Article> getUserNews(String userName) {

        Optional<NewsPreferences> newsPreferences = newsPreferencesRepository.findById(userName);

        if(newsPreferences.isEmpty()) {
            throw new NewsPreferenceNotFound("Couldn't find any user preference",userName);
        }

        NewsPreferences validatedNewsPreferences = newsPreferences.get();

        String preferredLanguage = validatedNewsPreferences.getLanguage();
        Boolean topicsEmpty = validatedNewsPreferences.getInterestedTopics().isEmpty();

        List<Article> articles = new ArrayList<>();

        if(topicsEmpty) {
            Everything everything = newsService.getEverything(preferredLanguage);

            if(everything.getStatus() == "error") {
                throw new RuntimeException("A provider-related error occurred");
            }

            if(!everything.getArticles().isEmpty()) {
                articles.addAll(everything.getArticles().stream().limit(12).collect(Collectors.toList()));
            }

            return articles;
        }

        String[] preferredTopics = validatedNewsPreferences.getInterestedTopics().split(","); // Splitting comma separated topics

        for(String preferredTopic : preferredTopics) {

            Everything everything = newsService.getEverything(preferredTopic,preferredLanguage);

            if(everything.getStatus() == "error") {
                throw new RuntimeException("A provider-related error occurred");
            }

            if(!everything.getArticles().isEmpty()) {
                articles.addAll(everything.getArticles().stream().limit(3).collect(Collectors.toList()));
            }

        }

        return articles;
    }
}
