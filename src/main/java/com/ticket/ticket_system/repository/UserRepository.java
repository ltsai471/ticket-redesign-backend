package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM user WHERE id = #{id}")
    Optional<User> findById(Long id);

    @Select("SELECT * FROM user WHERE name = #{name}")
    Optional<User> findByName(String name);

    @Insert("INSERT INTO user (name, age) VALUES (#{name}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(User user);
}

