package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {
    Optional<Ticket> findById(UUID id);
    List<Ticket> findByPaid(boolean paid);
//    Optional<Ticket> findByName(String name);
}
