package com.ticket.ticket_system.service;

import com.ticket.ticket_system.entity.Campaign;
import com.ticket.ticket_system.repository.CampaignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
    @Autowired
    CampaignRepository campaignRepository;

    private final static Logger log = LoggerFactory.getLogger(CampaignService.class);

    public String addCampaign(String name) {
        Campaign campaign = new Campaign(null, name);
        campaignRepository.save(campaign);
        log.info("Campaign: {}", campaign.getId());
        return String.format("save (%d, %s)", campaign.getId(), name);
    }

    public String getCampaign(String name) {
        StringBuilder result = new StringBuilder();
        Campaign campaign = campaignRepository.findByName(name);
        if (campaign != null) {
            result.append(campaign.getName());
            log.info("Campaign: {}", campaign.getId());
        }
        return result.toString();
    }
}



