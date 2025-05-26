package com.ticket.ticket_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TestService {
    private final static Logger log = LoggerFactory.getLogger(TestService.class);

    public String welcome() {
        return "Welcome to Ticket System!";
    }

    public String hello(String name) {
        return String.format("Hi %s!", name);
    }

}



