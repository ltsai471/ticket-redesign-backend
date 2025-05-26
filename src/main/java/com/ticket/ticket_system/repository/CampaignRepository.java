package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Campaign;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CampaignRepository extends CrudRepository<Campaign, UUID> {
    Optional<Campaign> findById(UUID id);
    Campaign findByName(String name);
}
