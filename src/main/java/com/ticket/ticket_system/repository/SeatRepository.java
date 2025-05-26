package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Seat;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface SeatRepository {
    @Select("SELECT * FROM seat WHERE id = #{id}")
    Optional<Seat> findById(Long id);

    @Select("SELECT * FROM seat WHERE campaign_id = #{campaignId} AND area = #{area} AND row = #{row} AND column = #{column}")
    Optional<Seat> findByKey(@Param("campaignId") Long campaignId, 
                           @Param("area") String area, 
                           @Param("row") int row, 
                           @Param("column") int column);

    @Insert("INSERT INTO seat (campaign_id, area, row, column, price, status) " +
            "VALUES (#{campaignId}, #{area}, #{row}, #{column}, #{price}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Seat seat);

    @Update("UPDATE seat SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
