package com.ticket.ticket_system.controller;

import com.ticket.ticket_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oper")
public class OperateController {
    @Autowired
    TicketService ticketService;
    @Autowired
    UserService userService;

    @GetMapping("/buyTicket")
    public String buyTicket(@RequestParam(value = "userId") String userId,
                            @RequestParam(value = "campaignName") String campaignName,
                            @RequestParam(value = "area") String area,
                            @RequestParam(value = "row") int row,
                            @RequestParam(value = "column") int column) {
        return ticketService.addTicket(userId, campaignName, area, row, column);
    }

    @GetMapping("/payTicket")
    public String payTicket(@RequestParam(value = "id") String id) {
        return ticketService.payTicket(id);
    }

    @GetMapping("/getUser")
    public String getUser(@RequestParam(value = "name") String name) {
        return userService.getUser(name);
    }

//
//    @GetMapping("/getAllStudents")
//    public String getAllStudents() {
//        return testService.getAllStudents();
//    }
}
