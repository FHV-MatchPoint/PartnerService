package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.CacheService;
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

    @Inject
    @CacheName("test-cache")


    @Override
    public String getCache() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        number += 1;
        return "Cache " + number;
    }
}
