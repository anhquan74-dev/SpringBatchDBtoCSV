package edu.dac.SpringBatchDBtoCSV.processor;

import org.springframework.batch.item.ItemProcessor;

import edu.dac.SpringBatchDBtoCSV.model.FacebookAds;

public class FacebookAdsItemProcessor implements ItemProcessor<FacebookAds, FacebookAds>{

	@Override
	public FacebookAds process(FacebookAds facebookAds) throws Exception {
		return facebookAds;
	}
}
