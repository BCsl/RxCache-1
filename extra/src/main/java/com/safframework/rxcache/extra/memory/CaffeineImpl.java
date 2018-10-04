package com.safframework.rxcache.extra.memory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Optional;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public class CaffeineImpl extends AbstractMemoryImpl {

    private Cache<String, Object> cache;

    public CaffeineImpl(long maxSize) {

        super(maxSize);
        cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .build();
    }

    public CaffeineImpl(long maxSize, CacheConfig cacheConfig) {

        super(maxSize);
        Caffeine caffeine = Caffeine.newBuilder()
                .maximumSize(maxSize);

        if (cacheConfig!=null) {

            if (cacheConfig.expireDuration>0 && cacheConfig.expireTimeUnit!=null) {

                caffeine.expireAfterWrite(cacheConfig.expireDuration,cacheConfig.expireTimeUnit);
            }

            if (cacheConfig.refreshDuration>0 && cacheConfig.refreshTimeUnit!=null) {

                caffeine.refreshAfterWrite(cacheConfig.refreshDuration,cacheConfig.refreshTimeUnit);
            }
        }

        cache = caffeine.build();
    }

    @Override
    public <T> CacheHolder<T> getIfPresent(String key) {

        Optional<T> optional = (Optional<T>) cache.getIfPresent(key);
        return optional != null ? new CacheHolder<>(optional.orNull(), timestampMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        cache.put(key,Optional.fromNullable(value));
        timestampMap.put(key,System.currentTimeMillis());
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

    }

    @Override
    public Set<String> keySet() {

        return cache.asMap().keySet();
    }

    @Override
    public boolean containsKey(String key) {

        return cache.asMap().containsKey(key);
    }

    @Override
    public void evict(String key) {

        cache.invalidate(key);
    }

    @Override
    public void evictAll() {

        cache.invalidateAll();
    }
}
