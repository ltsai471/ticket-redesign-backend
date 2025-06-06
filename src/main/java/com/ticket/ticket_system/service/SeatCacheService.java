package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.Collections;

@Service
public class SeatCacheService {
    @Autowired
    private SeatRepository seatRepository;

    private final RedisTemplate<String, Object> redis;
    private static final String SEAT_KEY_PREFIX = "seat:";
    private static final long BASE_CACHE_TTL = 5; // 基础过期时间
    private static final long RANDOM_RANGE = 2; // 随机范围

    public SeatCacheService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public List<Seat> getSeatAvailability(Long campaignId, String area) {
        String key = generateKey(campaignId, area);
        return (List<Seat>) redis.opsForValue().get(key);
    }

    public void clearSeatCacheByCampaignIdAndArea(Long campaignId, String area) {
        String key = generateKey(campaignId, area);
        redis.delete(key);
    }

    public void updateSeatStatus(Long campaignId, String area, Long seatId, String status) {
        String key = generateKey(campaignId, area);
        List<Seat> seats = getSeatAvailability(campaignId, area);
        if (seats != null) {
            // Update the seat status in the cached list
            seats.stream()
                .filter(seat -> {
                    // Assuming seat is a Map with 'id' field
                    return ((java.util.Map<String, Object>) seat).get("id").equals(seatId);
                })
                .findFirst()
                .ifPresent(seat -> {
                    ((java.util.Map<String, Object>) seat).put("status", status);
                });
            
            // Update the cache
            redis.opsForValue().set(key, seats, getRandomTTL(), TimeUnit.MINUTES);
        }
    }

    public void cacheSeatAvailability(Long campaignId, String area, List<Object> seats) {
        String key = generateKey(campaignId, area);
        redis.opsForValue().set(key, seats, getRandomTTL(), TimeUnit.MINUTES);
    }

    public List<Seat> getSeatAvailabilityWithLock(Long campaignId, String area) {
        String key = generateKey(campaignId, area);
        String lockKey = "lock:" + key;
        
        // 尝试获取缓存
        List<Seat> seats = (List<Seat>) redis.opsForValue().get(key);
        if (seats != null) {
            return seats;
        }
        
        // 获取分布式锁
        boolean locked = redis.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (locked) {
            try {
                // 双重检查
                seats = (List<Seat>) redis.opsForValue().get(key);
                if (seats != null) {
                    return seats;
                }
                
                // 从数据库加载数据
                seats = seatRepository.findByCampaignAndArea(campaignId, area);
                // 更新缓存
                if (seats != null) {
                    cacheSeatAvailability(campaignId, area, new ArrayList<>(seats));
                }
                return seats;
            } finally {
                // 释放锁
                redis.delete(lockKey);
            }
        }
        
        // 如果获取锁失败，等待一段时间后重试
        try {
            Thread.sleep(100);
            return getSeatAvailabilityWithLock(campaignId, area);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private String generateKey(Long campaignId, String area) {
        return SEAT_KEY_PREFIX + campaignId + ":" + area;
    }

    private long getRandomTTL() {
        return BASE_CACHE_TTL + new Random().nextInt((int) RANDOM_RANGE);
    }
} 