package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.entity.Ticket;
import com.ticket.ticket_system.repository.SeatRepository;
import com.ticket.ticket_system.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Random;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;

    private final static Logger log = LoggerFactory.getLogger(TicketService.class);

    public String addTicket(Long userId, Long campaignId, String area, int row, int column) {
        try {
            Optional<Seat> seat = seatRepository.findByKey(campaignId, area, row, column);
            if (!seat.isPresent()) return "error: no seat";

            // Check if seat is already reserved
            if (seat.get().getStatus().equals("reserved") || seat.get().getStatus().equals("purchased")) {
                return "{\"error\": \"SEAT_ALREADY_RESERVED\", \"message\": \"This seat has already been reserved. Please select another seat.\"}";
            }

            log.info("ticketRepository.save");
            Long seatId = seat.get().getId();
            Ticket ticket = new Ticket(null, userId, seatId, false, new Date());
            ticketRepository.create(ticket);
            seatRepository.updateStatus(seat.get().getId(), "reserved");
            return String.format("{\"ticketId\": %d}", ticket.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
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

    public String payTicket(Long id) {
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);
            if (ticket.isPresent()) {
                seatRepository.updateStatus(ticket.get().getSeatId(), "paying");
                
                // Simulate calling 3rd party payment API with 5 second delay
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new Exception("Payment process interrupted");
                }
                
                // Simulate random payment success/failure (30% chance of failure)
                Random random = new Random();
                boolean paymentSuccess = random.nextDouble() > 0.2;
                
                if (paymentSuccess) {
                    ticketRepository.updatePaidStatus(ticket.get().getId(), true);
                    seatRepository.updateStatus(ticket.get().getSeatId(), "sold");
                    return "{\"status\": \"SUCCESS\", \"message\": \"Payment successful\"}";
                } else {
                    // Handle failed payment
                    seatRepository.updateStatus(ticket.get().getSeatId(), "reserved");
                    return "{\"status\": \"FAILED\", \"message\": \"Payment failed. Please try again.\"}";
                }
            } else {
                throw new Exception("Error. Ticket#" + id + " is not found.");
            }
        } catch (Exception e) {
            log.error("payTicket", e.getMessage());
            return "{\"status\": \"ERROR\", \"message\": \"" + e.getMessage() + "\"}";
        }
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



