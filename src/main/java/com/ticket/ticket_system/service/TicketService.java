package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Campaign;
import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.entity.Ticket;
import com.ticket.ticket_system.repository.CampaignRepository;
import com.ticket.ticket_system.repository.SeatRepository;
import com.ticket.ticket_system.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    CampaignRepository campaignRepository;

    private final static Logger log = LoggerFactory.getLogger(TicketService.class);

    public String addTicket(Long userId, Long campaignId, String area, int row, int column) {
        try {
            Optional<Seat> seat = seatRepository.findByKey(campaignId, area, row, column);
            if (!seat.isPresent()) return "error: no seat";

            // Check if seat is already reserved
            if (seat.get().getStatus().equals("occupied") || seat.get().getStatus().equals("purchased")) {
                return "{\"error\": \"SEAT_ALREADY_RESERVED\", \"message\": \"This seat has already been reserved. Please select another seat.\"}";
            }

            log.info("ticketRepository.save");
            Long seatId = seat.get().getId();
            Ticket ticket = new Ticket(null, userId, seatId, false, new Date());
            ticketRepository.save(ticket);
            seatRepository.updateStatus(seat.get().getId(), "occupied");
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
            StringBuilder result = new StringBuilder();
            Optional<Ticket> ticket = ticketRepository.findById(id);
            if (ticket.isPresent()) {
                ticket.get().setPaid(true);
                ticketRepository.save(ticket.get());
                updateSeatStatus(ticket.get().getSeatId(), "purchased");
            } else {
                throw new Exception("Error. Ticket#" + id + " is not found.");
            }
            return result.toString();
        } catch (Exception e) {
            log.error("payTicket", e.getMessage());
            return e.getMessage();
        }
    }

    public String releaseTicket(Long id) {
        try {
            //check paid or not
            List<Ticket> unpaidTickets = ticketRepository.findByPaid(false);
            for (Ticket ticket : unpaidTickets) {
                Date date = ticket.getCreationDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE, 10);
                if (cal.getTime().before(new Date())) {
                    Optional<Seat> seat = seatRepository.findById(ticket.getSeatId());
                    seat.get().setStatus("purchased");
                    seatRepository.save(seat.get());
                }
            }
            return "saved.";
        } catch (Exception e) {
            log.error("releaseTicket", e.getMessage());
            return e.getMessage();
        }
    }

    private void updateSeatStatus(Long seatId, String status) {
        log.info("updateSeatStatus:" + seatId + "->" + status);
        Optional<Seat> seat = seatRepository.findById(seatId);
        if (seat.isPresent()) {
            seat.get().setStatus(status);
            seatRepository.save(seat.get());
        }
    }
}



