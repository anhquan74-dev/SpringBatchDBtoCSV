package edu.dac.SpringBatchDBtoCSV.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.dac.SpringBatchDBtoCSV.model.FacebookAds;

public class FacebookAdsRowMapper implements RowMapper<FacebookAds> {

	@Override
	public FacebookAds mapRow(ResultSet rs, int rowNum) throws SQLException {
		FacebookAds facebookAds = new FacebookAds();
		facebookAds.setDate(rs.getString("Date"));
		facebookAds.setMedia(rs.getString("Media"));
		facebookAds.setAdnameLPID(rs.getString("AdnameLPID"));
		facebookAds.setCost(rs.getDouble("Cost"));
		facebookAds.setImpression(rs.getInt("Impression"));
		facebookAds.setClick(rs.getInt("Click"));
		facebookAds.setCv(rs.getInt("CV"));
		return facebookAds;
	}

}