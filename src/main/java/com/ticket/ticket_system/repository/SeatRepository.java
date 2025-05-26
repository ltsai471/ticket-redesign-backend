package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.entity.SeatKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends CrudRepository<Seat, SeatKey> {
    Optional<Seat> findById(UUID id);

    //    Optional<Seat> findById(UUID id);
//    Seat findByCampaignIdAndAreaAndRowAndColumn(String campaignId, String area, int row, int column);
    Optional<Seat> findByKey(SeatKey key);
}
