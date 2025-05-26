package com.ticket.ticket_system.controller;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup")
public class SetupController {
    @Autowired
    UserService userService;
    @Autowired
    CampaignService campaignService;
    @Autowired
    SeatService seatService;

    @GetMapping("/setupUsers")
    public String setupUsers() {
        return userService.addDummyUsers();
    }

    @GetMapping("/addCampaign")
    public String addCampaign(@RequestParam(value = "name") String name) {
        return campaignService.addCampaign(name);
    }

    @GetMapping("/addSeatsByCampaign")
    public String addSeatsByCampaign(@RequestParam(value = "campaignId") String campaignId) {
        try {
            seatService.addSeats("A", 10, 10, 8000, campaignId);
            seatService.addSeats("B", 100, 100, 4000, campaignId);
            seatService.addSeats("C", 500, 500, 2000, campaignId);
            return "Done";
        } catch (Exception e) {
            return "Error";
        }
    }

    @GetMapping("/addSeatsByArea")
    public String addSeatsByArea(@RequestParam(value = "area") String area,
                                 @RequestParam(value = "rowNum") int rowNum,
                                 @RequestParam(value = "columnNum") int columnNum,
                                 @RequestParam(value = "price") int price,
                                 @RequestParam(value = "campaignId") String campaignId) {
        try {
            seatService.addSeats(area, rowNum, columnNum, price, campaignId);
            return "Done";
        } catch (Exception e) {
            return "Error";
        }
    }


}
