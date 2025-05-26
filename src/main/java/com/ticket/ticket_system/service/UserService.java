package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.User;
import com.ticket.ticket_system.repository.UserRepository;
import com.ticket.ticket_system.utils.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    public String addUser(String id, String name, int age) {
        User user = new User(id, name, age);
        userRepository.save(user);
        return String.format("save (%s, %s, %d)", user.getId(), name, age);
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
            int id = 1;
            for (String name : Names.names) {
                addUser("a_" + id, name, getRandomAge());
                id++;
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



