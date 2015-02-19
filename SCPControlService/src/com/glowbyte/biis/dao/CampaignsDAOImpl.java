package com.glowbyte.biis.dao;

import java.util.Hashtable;
import java.util.List;

import com.glowbyte.biis.domain.Campaign;
import com.glowbyte.biis.utils.DatabaseConnection;

public class CampaignsDAOImpl implements CampaignsDAO{

	@Override
	public List<Campaign> getCampaignById(int id) throws Exception {
		DatabaseConnection connection = DatabaseConnection.getInstance();
		Hashtable<String, String> resultSet=connection.selectFromDB("Select * from campaigns where camp_id= ?", String.valueOf(id));
		
		return null;
	}

}
