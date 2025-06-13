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

    @Autowired
    private TicketPurchaseStatusService statusService;

    @KafkaListener(topics = "ticket-purchase", groupId = "ticket-purchase-group")
    public void consumeTicketPurchase(@Payload TicketPurchaseRequest request,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            log.info("Processing ticket purchase request: {}", request);
            
            // Update status to PROCESSING
            statusService.updateStatus(request.getRequestId(), "PROCESSING", "Processing ticket purchase", null);
            
            // Process the ticket purchase
            String result = ticketService.addTicket(
                request.getUserId(),
                request.getCampaignId(),
                request.getArea(),
                request.getRow(),
                request.getColumn()
            );

            // If there was an error, send to DLQ and update status
            if (result.contains("error")) {
                statusService.updateStatus(request.getRequestId(), "FAILED", result.split(":")[1], null);
                request.setStatus("FAILED");
                request.setErrorMessage(result.split(":")[1]);
                kafkaTemplate.send("ticket-purchase-dlq", request);
                log.error("Failed to process ticket purchase: {}", result);
            } else {
                // Extract ticket ID from result
                Long ticketId = extractTicketId(result);
                statusService.updateStatus(request.getRequestId(), "COMPLETED", "Ticket purchase completed successfully", ticketId);
                log.info("Successfully processed ticket purchase: {}", result);
            }
        } catch (Exception e) {
            log.error("Error processing ticket purchase", e);
            statusService.updateStatus(request.getRequestId(), "FAILED", e.getMessage(), null);
            request.setStatus("FAILED");
            request.setErrorMessage(e.getMessage());
            kafkaTemplate.send("ticket-purchase-dlq", request);
        }
    }

    private Long extractTicketId(String result) {
        try {
            // Assuming result is in format {"ticketId": 123}
            return Long.parseLong(result.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            log.error("Error extracting ticket ID from result: {}", result, e);
            return null;
        }
    }

    @KafkaListener(topics = "ticket-purchase-dlq", groupId = "ticket-purchase-dlq-group")
    public void consumeFailedTicketPurchase(@Payload TicketPurchaseRequest request) {
        log.error("Received failed ticket purchase in DLQ: {}", request);
        // Here you could implement retry logic or alerting
    }
} 