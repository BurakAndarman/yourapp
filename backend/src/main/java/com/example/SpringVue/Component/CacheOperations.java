package com.example.SpringVue.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class CacheOperations {

    private static Logger log = LoggerFactory.getLogger(CacheOperations.class);

    @CacheEvict(value = "userNewsCache", key = "#userName")
    public void evictUserNews(String userName) {
        log.info("Evicted userNewsCache entry for key : "+userName);
    }

}
