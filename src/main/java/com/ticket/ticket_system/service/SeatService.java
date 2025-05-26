package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.entity.SeatKey;
import com.ticket.ticket_system.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;

    private final static Logger log = LoggerFactory.getLogger(SeatService.class);

    public void addSeat(String area, int row, int column, int price, String campaignId) throws Exception {
        try {
            log.info(campaignId);
            SeatKey key = new SeatKey(campaignId, area, row, column);
            Seat seat = new Seat(key, price, "absent", UUID.randomUUID());
            Seat savedSeat = seatRepository.save(seat);
            log.info("addSeat", String.format("save (%s, %s, %d)", savedSeat.getId(), area, row, column));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

//    public String getSeat(UUID id) {
//        StringBuilder result = new StringBuilder();
//        Optional<Seat> seat = seatRepository.findById(id);
//        if (seat.isPresent()) {
//            result.append(seat.get().getId() + " " + seat.get().getCampaignId());
//            log.info("Seat: {}", seat.get().getId());
//        }
//        return result.toString();
//    }

    public void addSeats(String area, int rowNum, int columnNum, int price, String campaignId) throws Exception {
        log.error("addSeats", area + ";" + rowNum + ";" + columnNum + ";" + price + ";" + campaignId);
        try {
            for (int rowId = 1; rowId <= rowNum; rowId++) {
                for (int colId = 1; colId <= columnNum; colId++) {
                    addSeat(area, rowId, colId + 1, price, campaignId);
                }
            }
        } catch (Exception e) {
            log.error("addSeats", e.getMessage());
            throw new Exception("addSeats" + e.getMessage());
        }
    }

}



