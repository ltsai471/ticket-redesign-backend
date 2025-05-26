package com.ticket.ticket_system.controller;

import com.ticket.ticket_system.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "test")
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestService testService;

    @Operation(summary = "welcome")
    @GetMapping("/welcome")
    public String welcome() {
        return testService.welcome();
    }

    @Operation(summary = "hello")
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name") String name) {
        return testService.hello(name);
    }

}
