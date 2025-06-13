package com.ticket.ticket_system.controller;

import com.ticket.ticket_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oper")
public class OperateController {
    @Autowired
    TicketService ticketService;
    @Autowired
    UserService userService;
    @Autowired
    TicketPurchaseProducer ticketPurchaseProducer;
    @Autowired
    TicketPurchaseStatusService ticketPurchaseStatusService;
    @Autowired
    PaymentService paymentService;

    @PostMapping("/buyTicket")
    public String buyTicket(@RequestBody TicketRequest request) {
        return ticketPurchaseProducer.produceTicketPurchase(request.getUserId(), request.getCampaignId(),
                request.getArea(), request.getRow(), request.getColumn());
    }

    @GetMapping("/getPurchaseStatus")
    public String getPurchaseStatus(@RequestParam(value = "requestId") String requestId) {
        return ticketPurchaseStatusService.getPurchaseStatus(requestId);
    }

    @GetMapping("/payTicket")
    public String payTicket(@RequestParam(value = "id") Long id) {
        return paymentService.payTicket(id);
    }

    @GetMapping("/getUser")
    public String getUser(@RequestParam(value = "name") String name) {
        return userService.getUser(name);
    }

}

class TicketRequest {
    private Long userId;
    private Long campaignId;
    private String area;
    private int row;
    private int column;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
}
