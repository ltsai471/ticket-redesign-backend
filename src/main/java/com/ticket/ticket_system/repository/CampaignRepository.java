package com.ticket.ticket_system.repository;

import com.ticket.ticket_system.entity.Campaign;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface CampaignRepository {
    @Select("SELECT * FROM campaign WHERE id = #{id}")
    Optional<Campaign> findById(Long id);

    @Select("SELECT * FROM campaign WHERE name = #{name}")
    Campaign findByName(String name);

    @Insert("INSERT INTO campaign (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Campaign campaign);
}
