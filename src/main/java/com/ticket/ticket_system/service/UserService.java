package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.User;
import com.ticket.ticket_system.repository.UserRepository;
import com.ticket.ticket_system.utils.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    public String addUser(String name, int age) {
        User user = new User(UUID.randomUUID(), name, age);
        User savedUser = userRepository.save(user);
        return String.format("save (%s, %s, %d)", savedUser.getId(), name, age);
    }

    public String getUser(String name) {
        StringBuilder result = new StringBuilder();
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            result.append(user.get().getName() + " " + user.get().getAge());
            log.info("User: {}", user.get().getId());
        }
        return result.toString();
    }

    public String addDummyUsers() {
        try {
            for (String name : Names.names) {
                addUser(name, getRandomAge());
            }
            return "Done";
        } catch (Exception e) {
            log.error("addDummyUsers", e.getMessage());
            return e.getMessage();
        }
    }

    private int getRandomAge() {
        return (int) (Math.random() * 100) + 1;
    }
}



