package com.example.SpringVue.Config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.cache.CacheManager;
import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager EhcacheManager() {

        CacheConfiguration<String, List> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class,List.class, ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .offheap(10, MemoryUnit.MB)
                        .build())
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(5)))
                .build();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        javax.cache.configuration.Configuration<String,List> configuration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig);
        cacheManager.createCache("userNewsCache",configuration);

        return cacheManager;
    }
}
