package com.safframework.rxcache.domain;

/**
 * Created by tony on 2018/10/26.
 */
public class MemoryCacheStatistics {

    private int size = 0;           // 缓存的数量
    private int putCount = 0;       // 缓存存放的数量
    private int evictionCount = 0;  // 缓存删除的数量
    private int hitCount = 0;       // 缓存命中的数量
    private int missCount = 0;      // 缓存没有命中的数量

    public MemoryCacheStatistics(int size) {

        this.size = size;
    }

    public int getSize() { return this.size; }

    public int getPutCount() { return this.putCount; }

    public int getEvictionCount() { return this.evictionCount; }

    public int getHitCount() { return this.hitCount; }

    public int getMissCount() { return this.missCount; }

    public void incrementPutCount() { this.putCount++; }

    public void incrementEvictionCount() { this.evictionCount++; }

    public void incrementHitCount() { this.hitCount++; }

    public void incrementMissCount() { this.missCount++; }
}
