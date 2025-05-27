package com.ticket.ticket_system.service;

import com.ticket.ticket_system.model.TicketPurchaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TicketPurchaseStatusService {
    private static final Logger log = LoggerFactory.getLogger(TicketPurchaseStatusService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STATUS_KEY_PREFIX = "ticket:purchase:status:";
    private static final long STATUS_EXPIRY_HOURS = 24;

    public String createPurchaseRequest(Long userId) {
        String requestId = UUID.randomUUID().toString();
        TicketPurchaseStatus status = new TicketPurchaseStatus(
                requestId,
                userId,
                null,
                "PENDING",
                "Purchase request created",
                new Date(),
                new Date()
        );

        redisTemplate.opsForValue().set(
                STATUS_KEY_PREFIX + requestId,
                status,
                STATUS_EXPIRY_HOURS,
                TimeUnit.HOURS
        );

        return requestId;
    }

    public String getPurchaseStatus(String requestId) {
        try {
            TicketPurchaseStatus status = getStatus(requestId);
            if (status == null) {
                return "{\"error\": \"NOT_FOUND\", \"message\": \"Purchase request not found.\"}";
            }
            return String.format("{\"status\": \"%s\", \"message\": \"%s\", \"ticketId\": %s}",
                    status.getStatus(),
                    status.getMessage(),
                    status.getTicketId() != null ? status.getTicketId() : "null");
        } catch (Exception e) {
            log.error("Error getting purchase status", e);
            return "{\"error\": \"SYSTEM_ERROR\", \"message\": \"An error occurred while checking purchase status.\"}";
        }
    }

    public void updateStatus(String requestId, String status, String message, Long ticketId) {
        TicketPurchaseStatus currentStatus = (TicketPurchaseStatus) redisTemplate.opsForValue().get(STATUS_KEY_PREFIX + requestId);
        if (currentStatus != null) {
            currentStatus.setStatus(status);
            currentStatus.setMessage(message);
            currentStatus.setTicketId(ticketId);
            currentStatus.setUpdatedAt(new Date());

            redisTemplate.opsForValue().set(
                    STATUS_KEY_PREFIX + requestId,
                    currentStatus,
                    STATUS_EXPIRY_HOURS,
                    TimeUnit.HOURS
            );
        }
    }

    public TicketPurchaseStatus getStatus(String requestId) {
        return (TicketPurchaseStatus) redisTemplate.opsForValue().get(STATUS_KEY_PREFIX + requestId);
    }
} 