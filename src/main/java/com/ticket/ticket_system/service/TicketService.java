package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.entity.Ticket;
import com.ticket.ticket_system.repository.SeatRepository;
import com.ticket.ticket_system.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final static Logger log = LoggerFactory.getLogger(TicketService.class);
    private static final String SEAT_LOCK_PREFIX = "seat:lock:";
    private static final long LOCK_TIMEOUT_MINUTES = 10;

    public String addTicket(Long userId, Long campaignId, String area, int row, int column) {
        String lockKey = SEAT_LOCK_PREFIX + campaignId + ":" + area + ":" + row + ":" + column;
        
        try {
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, userId, LOCK_TIMEOUT_MINUTES, TimeUnit.MINUTES);
            
            if (Boolean.FALSE.equals(acquired)) {
                return "error:SEAT_ALREADY_RESERVED";
            }

            try {
                Optional<Seat> seat = seatRepository.findByKey(campaignId, area, row, column);
                if (!seat.isPresent()) {
                    return "error:NO_SEAT";
                }

                if (seat.get().getStatus().equals("reserved") || seat.get().getStatus().equals("purchased")) {
                    return "error:SEAT_ALREADY_RESERVED";
                }

                Long seatId = seat.get().getId();
                Ticket ticket = new Ticket(null, userId, seatId, false, new Date());
                ticketRepository.create(ticket);
                seatRepository.updateStatus(seat.get().getId(), "reserved");
                return String.format("{\"ticketId\": %d}", ticket.getId());
            } finally {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            log.error("Error in addTicket: {}", e.getMessage());
            return "{\"error\": \"SYSTEM_ERROR\", \"message\": \"An error occurred while processing your request.\"}";
        }
    }

    public String getTicket(Long id) {
        StringBuilder result = new StringBuilder();
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            result.append(ticket.get().getId() + " " + ticket.get().isPaid());
            log.info("Ticket: {}", ticket.get().getId() + " " + ticket.get().isPaid());
        }
        return result.toString();
    }

    public String releaseTicket() {
        try {
            // Get all unpaid tickets
            List<Ticket> unpaidTickets = ticketRepository.findByPaid(false);
            Date currentTime = new Date();
            
            for (Ticket ticket : unpaidTickets) {
                // Calculate expiration time (10 minutes after creation)
                Calendar cal = Calendar.getInstance();
                cal.setTime(ticket.getCreationDate());
                cal.add(Calendar.MINUTE, 10);
                Date expirationTime = cal.getTime();
                
                // Check if ticket has expired
                if (currentTime.after(expirationTime)) {
                    Optional<Seat> seat = seatRepository.findById(ticket.getSeatId());
                    if (seat.isPresent() && "reserved".equals(seat.get().getStatus())) {
                        seatRepository.updateStatus(seat.get().getId(), "available");
                        
                        // Delete the expired ticket
                        ticketRepository.updateCancelDate(ticket.getId());
                        log.info("Released expired ticket {} and seat {}", ticket.getId(), seat.get().getId());
                    }
                }
            }
            return "Ticket release process completed successfully.";
        } catch (Exception e) {
            log.error("Error in releaseTicket", e);
            return "Error: " + e.getMessage();
        }
    }

}



