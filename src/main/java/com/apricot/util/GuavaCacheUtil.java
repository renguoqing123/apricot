package com.apricot.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.Maps;

import lombok.Builder;

@Builder
public class GuavaCacheUtil {

	public static Map<String, Cache<Object, Object>> mapCache = Maps.newConcurrentMap();

	public volatile Cache<Object, Object> cache;

	public static String cacheKey;

	public GuavaCacheUtil create(String key) {
		cacheKey = key;
		setCache(CacheBuilder.newBuilder()
				.initialCapacity(32)
				.maximumSize(1024)
				.expireAfterWrite(10, TimeUnit.SECONDS)
				.build());
		return this;
	}

	public GuavaCacheUtil create(String key, int duration, TimeUnit unit) {
		return extracted(key, 32, 64, 1024, duration, unit);
	}

	public GuavaCacheUtil create(String key, int initialCapacity, int concurrencyLevel, int maximumSize, 
			int duration, TimeUnit unit) {
		return extracted(key, initialCapacity, concurrencyLevel, maximumSize, duration, unit);
	}

	private GuavaCacheUtil extracted(String key, int initialCapacity, int concurrencyLevel, int maximumSize,
			int duration, TimeUnit unit) {
		cacheKey = key;
		setCache(CacheBuilder.newBuilder()
				.initialCapacity(initialCapacity)// 初始化容器
				.concurrencyLevel(concurrencyLevel)// 并发级别
				.maximumSize(maximumSize)// 缓存最大容量
				.expireAfterWrite(duration, unit)
				.build());
		return this;
	}

	public static Object get(String key) {
		Cache<Object, Object> c = mapCache.get(key);
		if (c == null) {
			return null;
		}
		return c.getIfPresent(key);
	}
	
	public void add(final Object value) {
		this.put(cacheKey, value);
	}

	public void put(String key, Object value) {
		extractedPut(key, value);
	}

	private void extractedPut(String key, Object value) {
		Cache<Object, Object> c = mapCache.get(key);
		if (c == null) {
			getCache().put(key, value);
			mapCache.put(key, cache);
		} else {
			c.put(key, value);
			mapCache.put(key, c);
		}
	}

	public static void del(String key) {
		Cache<Object, Object> c = mapCache.get(key);
		if (c == null) {
			return;
		} else {
			mapCache.remove(key);
			c.invalidate(key);
		}
	}

	// 查看命中的缓存数量、没有命中的缓存梳理、删除的缓存梳理
	public CacheStats getCacheStats() {
		return getCache().stats();
	}

	public Cache<Object, Object> getCache() {
		return mapCache.get(cacheKey);
	}

	public void setCache(Cache<Object, Object> cache) {
		this.cache = cache;
		mapCache.put(cacheKey, this.cache);
	}}
