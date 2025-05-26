package com.ticket.ticket_system.repository;

import java.util.Optional;
import java.util.UUID;

import com.ticket.ticket_system.entity.TestStudent;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<TestStudent, UUID> {
    Optional<TestStudent> findByName(String username);
}
