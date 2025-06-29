package com.example.SpringVue.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.SpringVue.Dto.WeatherApi.Forecast.ForecastWrapper;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.cache.CacheManager;
import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableCaching
public class AppConfig {

    private final String cloudinaryCloudName;

    private final String cloudinaryApiKey;

    private final String cloudinaryApiSecret;

    public AppConfig(@Value("${utility-apis.cloudinary-cloud-name}") String cloudinaryCloudName,
                     @Value("${utility-apis.cloudinary-api-key}") String cloudinaryApiKey,
                     @Value("${utility-apis.cloudinary-api-secret}") String cloudinaryApiSecret) {
        this.cloudinaryCloudName = cloudinaryCloudName;
        this.cloudinaryApiKey = cloudinaryApiKey;
        this.cloudinaryApiSecret = cloudinaryApiSecret;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryCloudName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecret
        ));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager jCacheManager = cachingProvider.getCacheManager();

        CacheConfiguration<String, HashMap> newsCacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, HashMap.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(3)))
                .build();

        CacheConfiguration<String, ForecastWrapper> forecastCacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, ForecastWrapper.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1)))
                .build();

        javax.cache.configuration.Configuration<String, HashMap> newsCacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(newsCacheConfig);
        javax.cache.configuration.Configuration<String, ForecastWrapper> forecastCacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(forecastCacheConfig);

        jCacheManager.createCache("userNewsCache", newsCacheConfiguration);
        jCacheManager.createCache("forecastCache", forecastCacheConfiguration);

        return new JCacheCacheManager(jCacheManager);
    }
}
