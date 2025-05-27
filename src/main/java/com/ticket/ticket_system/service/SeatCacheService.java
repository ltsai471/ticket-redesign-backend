package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeatCacheService {

    private final RedisTemplate<String, Object> redis;
    private static final String SEAT_KEY_PREFIX = "seat:";
    private static final long CACHE_TTL = 5; // Cache TTL in minutes

    public SeatCacheService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public void cacheSeatAvailability(Long campaignId, String area, List<Object> seats) {
        String key = generateKey(campaignId, area);
        redis.opsForValue().set(key, seats, CACHE_TTL, TimeUnit.MINUTES);
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
            redis.opsForValue().set(key, seats, CACHE_TTL, TimeUnit.MINUTES);
        }
    }

    private String generateKey(Long campaignId, String area) {
        return SEAT_KEY_PREFIX + campaignId + ":" + area;
    }
} 