package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Seat;
import com.ticket.ticket_system.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;

    private final static Logger log = LoggerFactory.getLogger(SeatService.class);

    public void addSeat(String area, int row, int column, int price, Long campaignId) throws Exception {
        try {
            Seat seat = new Seat(campaignId, area, row, column, price, "absent", null);
            seatRepository.save(seat);
            log.info("addSeat", String.format("save (%d, %s, %d)", seat.getId(), area, row));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public void addSeats(String area, int rowNum, int columnNum, int price, Long campaignId) throws Exception {
        log.info("addSeats: area={}, rowNum={}, columnNum={}, price={}, campaignId={}", 
                area, rowNum, columnNum, price, campaignId);
        try {
            List<Seat> seats = new ArrayList<>();
            for (int rowId = 1; rowId <= rowNum; rowId++) {
                for (int colId = 1; colId <= columnNum; colId++) {
                    seats.add(new Seat(campaignId, area, rowId, colId, price, "absent", null));
                }
            }
            seatRepository.batchSave(seats);
            log.info("Successfully added {} seats", seats.size());
        } catch (Exception e) {
            log.error("addSeats failed: {}", e.getMessage());
            throw new Exception("addSeats failed: " + e.getMessage());
        }
    }

    public List<Seat> getSeatsByArea(Long campaignId, String area) {
        log.info("Getting seats for campaign {} and area {}", campaignId, area);
        return seatRepository.findByCampaignAndArea(campaignId, area);
    }
}



