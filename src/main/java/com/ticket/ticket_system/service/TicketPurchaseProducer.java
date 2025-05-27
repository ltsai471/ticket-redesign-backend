package com.ticket.ticket_system.service;

import com.ticket.ticket_system.model.TicketPurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketPurchaseProducer {
    private static final Logger log = LoggerFactory.getLogger(TicketPurchaseProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private TicketPurchaseStatusService statusService;

    public String produceTicketPurchase(Long userId, Long campaignId, String area, int row, int column) {
        try {
            String requestId = statusService.createPurchaseRequest(userId);
            TicketPurchaseRequest request = new TicketPurchaseRequest(
                    requestId, userId, campaignId, area, row, column, null, "PENDING", null
            );
            kafkaTemplate.send("ticket-purchase", request);
            return String.format("{\"status\": \"PENDING\", \"requestId\": \"%s\", \"message\": \"Ticket purchase request has been queued for processing.\"}", requestId);

        } catch (Exception e) {
            log.error(e.getMessage());
            return "{\"error\": \"SYSTEM_ERROR\", \"message\": \"An error occurred while processing your request.\"}";
        }
    }

} 