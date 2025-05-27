package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Seat;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SeatRepository {
    @Select("SELECT * FROM seat WHERE id = #{id}")
    Optional<Seat> findById(Long id);

    @Select("SELECT * FROM seat WHERE campaign_id = #{campaignId} AND area = #{area} AND seat_row = #{seatRow} AND seat_column = #{seatColumn}")
    Optional<Seat> findByKey(@Param("campaignId") Long campaignId, 
                           @Param("area") String area, 
                           @Param("seatRow") int seatRow,
                           @Param("seatColumn") int seatColumn);

    @Insert("INSERT INTO seat (campaign_id, area, seat_row, seat_column, price, status) " +
            "VALUES (#{campaignId}, #{area}, #{seatRow}, #{seatColumn}, #{price}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Seat seat);

    @Insert("<script>" +
            "INSERT INTO seat (campaign_id, area, seat_row, seat_column, price, status) VALUES " +
            "<foreach collection='seats' item='seat' separator=','>" +
            "(#{seat.campaignId}, #{seat.area}, #{seat.seatRow}, #{seat.seatColumn}, #{seat.price}, #{seat.status})" +
            "</foreach>" +
            "</script>")
    void batchSave(@Param("seats") List<Seat> seats);

    @Update("UPDATE seat SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT * FROM seat WHERE campaign_id = #{campaignId} AND area = #{area} ORDER BY seat_row, seat_column")
    List<Seat> findByCampaignAndArea(@Param("campaignId") Long campaignId, @Param("area") String area);
}
