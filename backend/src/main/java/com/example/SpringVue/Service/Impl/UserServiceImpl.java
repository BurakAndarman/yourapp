package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.NewsApi.TopHeadlines.Article;
import com.example.SpringVue.Dto.NewsApi.TopHeadlines.TopHeadlines;
import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.Request.UpdateNewsPreferencesRequest;
import com.example.SpringVue.Dto.Request.SavePlansRequest;
import com.example.SpringVue.Dto.Response.GetNewsPreferencesResponse;
import com.example.SpringVue.Dto.Response.GetPlansResponse;
import com.example.SpringVue.Entity.NewsPreferences;
import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Exception.DuplicateUsername;
import com.example.SpringVue.Exception.NewsPreferenceNotFound;
import com.example.SpringVue.Exception.UserNotFound;
import com.example.SpringVue.Repo.NewsPreferencesRepository;
import com.example.SpringVue.Repo.PlansRepository;
import com.example.SpringVue.Repo.UserRepository;
import com.example.SpringVue.Dto.Request.SaveUserRequest;
import com.example.SpringVue.Service.NewsService;
import com.example.SpringVue.Service.UserService;
import com.example.SpringVue.Utils.EvictCache;
import com.example.SpringVue.Utils.KanbanList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDetailsManager userDetailsManager;

    private final NewsService newsService;

    private final NewsPreferencesRepository newsPreferencesRepository;

    private final UserRepository userRepository;

    private final PlansRepository plansRepository;

    private final EvictCache evictCache;

    public UserServiceImpl(UserDetailsManager userDetailsManager, NewsService newsService, NewsPreferencesRepository newsPreferencesRepository, UserRepository userRepository, PlansRepository plansRepository, EvictCache evictCache){
        this.userDetailsManager = userDetailsManager;
        this.newsService = newsService;
        this.newsPreferencesRepository = newsPreferencesRepository;
        this.userRepository = userRepository;
        this.plansRepository = plansRepository;
        this.evictCache = evictCache;
    }

    @Override
    public String addUser(SaveUserRequest saveUserRequest) {

        Optional<com.example.SpringVue.Entity.User> userCheck = userRepository.findById(saveUserRequest.getUserName());

        if(userCheck.isPresent()) {
            throw new DuplicateUsername("There is already a user with the same username");
        }

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
    public GetNewsPreferencesResponse getNewsPreferences(String userName) {

        Optional<NewsPreferences> newsPreferences = newsPreferencesRepository.findById(userName);

        if(newsPreferences.isEmpty()) {
            throw new NewsPreferenceNotFound("Couldn't find any user preference",userName);
        }

        String language = newsPreferences.get().getLanguage();
        List<String> interestedTopics = new ArrayList<>();

        if(!newsPreferences.get().getInterestedTopics().isEmpty()) {
            interestedTopics.addAll(Arrays.stream(newsPreferences.get().getInterestedTopics().split(",")).toList());
        }

        return new GetNewsPreferencesResponse(language, interestedTopics);
    }

    @Override
    public String updateNewsPreferences(UpdateNewsPreferencesRequest updateNewsPreferencesRequest, String userName) {

        String language = updateNewsPreferencesRequest.getLanguage();
        String interestedTopics = "";

        if(!updateNewsPreferencesRequest.getInterestedTopics().isEmpty()) {
            interestedTopics = String.join(",", updateNewsPreferencesRequest.getInterestedTopics());
        }

        NewsPreferences newsPreferences = new NewsPreferences(
                                            userName,
                                            language,
                                            interestedTopics
                                          );

        newsPreferencesRepository.save(newsPreferences);

        evictCache.evictUserNews(userName);

        return "Changes recorded successfully";

    }

    @Override
    public List<PlansDto> getPlans(String userName) {

        Optional<com.example.SpringVue.Entity.User> user = userRepository.findById(userName);

        if(user.isEmpty()) {
            throw new UserNotFound("Invalid username");
        }

        List<Plans> plans = plansRepository.findAllByUser(user.get());

        List<PlansDto> plansDtos = plans.stream().map(userPlan -> {
            List<String> tags = new ArrayList<>();

            if(!userPlan.getTags().isEmpty()) {
                tags.addAll(Arrays.stream(userPlan.getTags().split(",")).toList());
            }

            return new PlansDto(
                   userPlan.getId(),
                   userPlan.getTitle(),
                   userPlan.getContent(),
                   tags,
                   userPlan.getKanbanList()
            );
        }).toList();

        return plansDtos;
    }

    @Override
    public String savePlans(SavePlansRequest savePlansRequest, String userName) {

        Optional<com.example.SpringVue.Entity.User> user = userRepository.findById(userName);

        if(user.isEmpty()) {
            throw new UserNotFound("Invalid username");
        }

        String tags = "";

        if(!savePlansRequest.getTags().isEmpty()) {
            tags = String.join(",", savePlansRequest.getTags());
        }

        plansRepository.save(new Plans(
                savePlansRequest.getTitle(),
                savePlansRequest.getContent(),
                tags,
                savePlansRequest.getKanbanList(),
                user.get()
        ));

        return "New plan recorded successfully";
    }

    @Cacheable(value = "userNewsCache", key = "#userName")
    @Override
    public List<Article> getUserNews(String userName) {

        log.info("Trying to fetch data from 3rd party api");

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
                articles.addAll(topHeadlines.getArticles().stream().limit(12).toList());
            }

            return articles;
        }

        String[] preferredTopics = validatedNewsPreferences.getInterestedTopics().split(","); // Splitting comma separated topics

        for(String preferredTopic : preferredTopics) {

            TopHeadlines topHeadlines = newsService.getTopHeadlines(preferredTopic,preferredLanguage);

            if(!topHeadlines.getArticles().isEmpty()) {
                articles.addAll(topHeadlines.getArticles().stream().limit(6).toList());
            }

        }

        return articles;
    }


}
