package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.CacheService;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Justin Ströhle
 * 07.05.2023
 */

@ApplicationScoped
public class CacheServiceImpl implements CacheService {

    private static int number = 0;


    @CacheResult(cacheName = "cache-test")
    @Override
    public String getCache() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Cache " + number;
    }

    @CacheInvalidate(cacheName = "cache-test")
    @Override
    public int updateCache() {
        number += 1;
        return number;
    }
}
