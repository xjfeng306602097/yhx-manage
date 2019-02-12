package com.yhx.redis.ratelimit.service;

import com.yhx.redis.lock.service.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterFactory {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private DistributedLock distributedLock;

	/**
	 * 本地持有对象
	 */
	private volatile Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

	/**
	 * @param key
	 *            redis key
	 * @param permitsPerSecond
	 *            每秒产生的令牌数
	 * @param maxBurstSeconds
	 *            最大存储多少秒的令牌
	 * @return
	 */
	public RateLimiter build(String key, Double permitsPerSecond, Integer maxBurstSeconds) {
		if (!rateLimiterMap.containsKey(key)) {
			synchronized (this) {
				if (!rateLimiterMap.containsKey(key)) {
					rateLimiterMap.put(key,
							new RateLimiter(key, permitsPerSecond, maxBurstSeconds, distributedLock, redisTemplate));
				}
			}
		}
		return rateLimiterMap.get(key);
	}

}
