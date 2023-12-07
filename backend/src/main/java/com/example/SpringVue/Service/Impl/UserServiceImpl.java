package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Entity.NewsPreferences;
import com.example.SpringVue.Exception.NewsPreferenceNotFound;
import com.example.SpringVue.Repo.NewsPreferencesRepository;
import com.example.SpringVue.Repo.UserRepository;
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

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsManager userDetailsManager;

    private final NewsService newsService;

    private final NewsPreferencesRepository newsPreferencesRepository;

    private final UserRepository userRepository;

    public UserServiceImpl(UserDetailsManager userDetailsManager, NewsService newsService, NewsPreferencesRepository newsPreferencesRepository, UserRepository userRepository){
        this.userDetailsManager = userDetailsManager;
        this.newsService = newsService;
        this.newsPreferencesRepository = newsPreferencesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String addUser(SaveUserRequest saveUserRequest) {

        UserDetails user = User.withDefaultPasswordEncoder()
                                .username(saveUserRequest.getUserName())
                                .password(saveUserRequest.getPassword())
                                .authorities("REGULAR") // There is only one type of user for now
                                .build();

        userDetailsManager.createUser(user);

        Optional<com.example.SpringVue.Entity.User> userFromDatabase = userRepository.findById(user.getUsername());

        if(userFromDatabase.isPresent()) {
            newsPreferencesRepository.save(new NewsPreferences(user.getUsername(),userFromDatabase.get()));
        }

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
            TopHeadlines topHeadlines = newsService.getTopHeadlines(preferredLanguage);

            if(!topHeadlines.getArticles().isEmpty()) {
                articles.addAll(topHeadlines.getArticles());
            }

            return articles;
        }

        String[] preferredTopics = validatedNewsPreferences.getInterestedTopics().split(","); // Splitting comma separated topics

        for(String preferredTopic : preferredTopics) {

            TopHeadlines topHeadlines = newsService.getTopHeadlines(preferredTopic,preferredLanguage);

            if(!topHeadlines.getArticles().isEmpty()) {
                articles.addAll(topHeadlines.getArticles());
            }

        }

        return articles;
    }
}
