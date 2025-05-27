package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Ticket;
import com.ticket.ticket_system.repository.SeatRepository;
import com.ticket.ticket_system.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PaymentService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;

    private final static Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Transactional
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

                // Simulate random payment success/failure (5% chance of failure)
                Random random = new Random();
                boolean paymentSuccess = random.nextDouble() > 0.05;

                if (paymentSuccess) {
                    ticketRepository.updatePaidStatus(ticket.get().getId(), true);
                    seatRepository.updateStatus(ticket.get().getSeatId(), "sold");
                    return "{\"status\": \"SUCCESS\", \"message\": \"Payment successful\"}";
                } else {
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

}



