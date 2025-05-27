package com.ticket.ticket_system.scheduler;

import com.ticket.ticket_system.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketReleaseScheduler {
    private static final Logger log = LoggerFactory.getLogger(TicketReleaseScheduler.class);

    @Autowired
    private TicketService ticketService;

    @Scheduled(fixedRate = 120000) // 120000 ms = 2 minutes
    public void scheduleTicketRelease() {
        log.info("Running scheduled ticket release check");
        String result = ticketService.releaseTicket();
        log.info("Ticket release result: {}", result);
    }
} 