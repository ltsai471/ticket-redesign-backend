package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Seat;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface SeatRepository {
    @Select("SELECT * FROM seat WHERE id = #{id}")
    Optional<Seat> findById(Long id);

    @Select("SELECT * FROM seat WHERE campaign_id = #{campaignId} AND area = #{area} AND seat_row = #{row} AND seat_column = #{column}")
    Optional<Seat> findByKey(@Param("campaignId") Long campaignId, 
                           @Param("area") String area, 
                           @Param("seat_row") int row,
                           @Param("seat_column") int column);

    @Insert("INSERT INTO seat (campaign_id, area, seat_row, seat_column, price, status) " +
            "VALUES (#{campaignId}, #{area}, #{seat_row}, #{seat_column}, #{price}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Seat seat);

    @Update("UPDATE seat SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
