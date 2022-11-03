package edu.dac.SpringBatchDBtoCSV.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import edu.dac.SpringBatchDBtoCSV.model.FacebookAds;
import edu.dac.SpringBatchDBtoCSV.processor.FacebookAdsItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<FacebookAds> reader() {
		JdbcCursorItemReader<FacebookAds> cursorItemReader = new JdbcCursorItemReader<>();
		cursorItemReader.setDataSource(dataSource);
		cursorItemReader.setSql("SELECT * FROM facebook_ads");
		cursorItemReader.setRowMapper(new FacebookAdsRowMapper());
		return cursorItemReader;
	}

	@Bean
	public FacebookAdsItemProcessor processor() {
		return new FacebookAdsItemProcessor();
	}

	@Bean
	public FlatFileItemWriter<FacebookAds> writer() {
		FlatFileItemWriter<FacebookAds> writer = new FlatFileItemWriter<FacebookAds>();
		writer.setResource(new ClassPathResource("facebook-ads.csv"));

		DelimitedLineAggregator<FacebookAds> lineAggregator = new DelimitedLineAggregator<FacebookAds>();
		lineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<FacebookAds> fieldExtractor = new BeanWrapperFieldExtractor<FacebookAds>();
		fieldExtractor.setNames(new String[] { "date", "media", "adnameLPID", "cost", "impression", "click","cv" });
		lineAggregator.setFieldExtractor(fieldExtractor);

		writer.setLineAggregator(lineAggregator);
		return writer;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<FacebookAds, FacebookAds>chunk(100).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

	@Bean
	public Job exportFacebookAdsJob() {
		return jobBuilderFactory.get("exportFacebookAdsJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}
}
