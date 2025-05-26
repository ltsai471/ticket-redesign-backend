package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM user WHERE id = #{id}")
    Optional<User> findById(String id);

    @Select("SELECT * FROM user WHERE name = #{name}")
    Optional<User> findByName(String name);

    @Insert("INSERT INTO user (id, name, age) " +
            "VALUES (#{id}, #{name}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);
}

