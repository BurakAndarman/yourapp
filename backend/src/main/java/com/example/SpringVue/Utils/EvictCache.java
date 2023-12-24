package com.example.SpringVue.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class EvictCache {

    private static Logger log = LoggerFactory.getLogger(EvictCache.class);

    @CacheEvict(value = "userNewsCache", key = "#userName")
    public void evictUserNews(String userName) {
        log.info("Evicted userNewsCache entry for key : "+userName);
    }

}
