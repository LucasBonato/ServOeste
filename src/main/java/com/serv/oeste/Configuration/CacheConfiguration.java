package com.serv.oeste.Configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfiguration {
    private static final int CINCO_SEGUNDOS = 5 * 1000;
    @Bean
    public CacheManager cacheManager(){
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        cacheManager.setCacheNames(Arrays.asList("tecnicoCache", "allTecnicos"));
        return cacheManager;
    }
    @CacheEvict(value = "tecnicoCache", allEntries = true)
    @Scheduled(fixedDelay = CINCO_SEGUNDOS, initialDelay = 0)
    public void evictTecnicoCache(){ }
    @CacheEvict(value = "allTecnicos", allEntries = true)
    @Scheduled(fixedDelay = CINCO_SEGUNDOS, initialDelay = 0)
    public void evictAllTecnicosCache(){ }
}
