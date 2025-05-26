package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.TestStudent;
import com.ticket.ticket_system.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestService {
    @Autowired
    StudentRepository studentRepository;

    private final static Logger log = LoggerFactory.getLogger(TestService.class);

    public String welcome() {
        return "Welcome to Ticket System!";
    }

    public String hello(String name) {
        return String.format("Hi %s!", name);
    }

    public String addStudent(String name, int age) {
        TestStudent john = new TestStudent(UUID.randomUUID(), name, age);
        TestStudent savedJohn = studentRepository.save(john);
        return String.format("save (%s, %s, %d)", savedJohn.getId(), name, age);
    }

    public String getAllStudents() {
        StringBuilder result = new StringBuilder();
        Iterable<TestStudent> students = studentRepository.findAll();
        for (TestStudent s : students) {
            result.append(s.getName() + " " + s.getAge());
            log.info("Student: {}", s.getName());
        }
        return result.toString();
    }
}



