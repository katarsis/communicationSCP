package com.glowbyte.biis.dao;

import java.util.List;

import com.glowbyte.biis.domain.Campaign;

public interface CampaignsDAO {

	public List<Campaign> getCampaignById(int id) throws Exception;
}
