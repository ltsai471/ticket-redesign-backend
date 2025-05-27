package com.ticket.ticket_system.service;

import com.ticket.ticket_system.model.TicketPurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class TicketPurchaseConsumer {
    private static final Logger log = LoggerFactory.getLogger(TicketPurchaseConsumer.class);

    @Autowired
    private TicketService ticketService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "ticket-purchase", groupId = "ticket-purchase-group")
    public void consumeTicketPurchase(@Payload TicketPurchaseRequest request,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            log.info("Processing ticket purchase request: {}", request);
            
            // Process the ticket purchase
            String result = ticketService.addTicket(
                request.getUserId(),
                request.getCampaignId(),
                request.getArea(),
                request.getRow(),
                request.getColumn()
            );

            // If there was an error, send to DLQ
            if (result.contains("error")) {
                request.setStatus("FAILED");
                request.setErrorMessage(result);
                kafkaTemplate.send("ticket-purchase-dlq", request);
                log.error("Failed to process ticket purchase: {}", result);
            } else {
                log.info("Successfully processed ticket purchase: {}", result);
            }
        } catch (Exception e) {
            log.error("Error processing ticket purchase", e);
            request.setStatus("FAILED");
            request.setErrorMessage(e.getMessage());
            kafkaTemplate.send("ticket-purchase-dlq", request);
        }
    }

    @KafkaListener(topics = "ticket-purchase-dlq", groupId = "ticket-purchase-dlq-group")
    public void consumeFailedTicketPurchase(@Payload TicketPurchaseRequest request) {
        log.error("Received failed ticket purchase in DLQ: {}", request);
        // Here you could implement retry logic or alerting
    }
} 