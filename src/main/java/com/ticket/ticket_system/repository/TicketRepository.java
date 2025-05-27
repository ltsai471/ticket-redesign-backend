package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TicketRepository {
    @Select("SELECT * FROM ticket WHERE id = #{id}")
    Optional<Ticket> findById(Long id);

    @Select("SELECT * FROM ticket WHERE paid = #{paid}")
    List<Ticket> findByPaid(boolean paid);

    @Insert("INSERT INTO ticket (user_id, seat_id, paid, creation_date) " +
            "VALUES (#{userId}, #{seatId}, #{paid}, #{creationDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Ticket ticket);

    @Update("UPDATE ticket SET paid = #{paid} WHERE id = #{id}")
    void updatePaidStatus(@Param("id") Long id, @Param("paid") boolean paid);

    @Update("UPDATE ticket SET cancel_date = NOW() WHERE id = #{id}")
    void updateCancelDate(@Param("id") Long id);
}
