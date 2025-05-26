package com.ticket.ticket_system.controller;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("/{campaignId}/{area}")
    public List<Seat> getSeatsByArea(@PathVariable Long campaignId, @PathVariable String area) {
        return seatService.getSeatsByArea(campaignId, area);
    }
} 