package es.elovendo.config;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.User;
import es.elovendo.rest.util.ItemRowMapper;
import es.elovendo.rest.util.UserRowMapper;
import es.elovendo.util.Constant;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

	Logger logger = Logger.getLogger(BatchConfiguration.class.getName());

	@Autowired
	private Environment environment;

	@Autowired
	private JdbcCursorItemReader<Item> placeItemReader;
	@Autowired
	private JdbcCursorItemReader<User> userItemReader;

	@Bean
	public JdbcCursorItemReader<Item> itemItemReader(DataSource dataSource) throws SQLException {
		JdbcCursorItemReader<Item> itemReader = new JdbcCursorItemReader<>();
		itemReader.setSql("SELECT itemid FROM item");
		itemReader.setDataSource(dataSource);
		itemReader.setRowMapper(new ItemRowMapper());
		return itemReader;
	}

	@Bean
	public JdbcCursorItemReader<User> userItemReader(DataSource dataSource) {
		JdbcCursorItemReader<User> itemReader = new JdbcCursorItemReader<>();
		itemReader.setSql("SELECT userid, login FROM userprofile");
		itemReader.setDataSource(dataSource);
		itemReader.setRowMapper(new UserRowMapper());
		return itemReader;
	}

	/**
	 * @throws Exception
	 */
	@Scheduled(cron = "00 00 * * * *") // Everyday at midnight
	public void doStuff() throws Exception {

		WebSitemapGenerator sitemapGenerator = sitemapGenerator(environment);

		logger.info("Started an update for sitemap...");

		// Put static pages first
		sitemapGenerator.addUrl(Constant.URL_ROOT_PATH);
		WebSitemapUrl aboutUrl = new WebSitemapUrl.Options(Constant.URL_ROOT_PATH + "/about").lastMod("2014-12-03T20:43:48.108+01:00")
				.priority(1.0).changeFreq(ChangeFreq.YEARLY).build();
		sitemapGenerator.addUrl(aboutUrl);

		// Generate indexed sitemaps with 1000 urls per sitemap
		/* Item */
		placeItemReader.open(new ExecutionContext());
		Item item = null;
		while ((item = placeItemReader.read()) != null) {
			String urlItem = "http://www.elovendo.com/bazaar/item/" + item.getItemId();
			WebSitemapUrl url = new WebSitemapUrl.Options(urlItem).lastMod(Calendar.getInstance().getTime())
					.priority(0.7).changeFreq(ChangeFreq.MONTHLY).build();
			sitemapGenerator.addUrl(url);
		}
		placeItemReader.close();

		/* User */
		userItemReader.open(new ExecutionContext());
		User user = null;
		while ((user = userItemReader.read()) != null) {

			String urlItem = "http://www.elovendo.com/site/public/" + user.getUserId() + "/" + user.getLogin();
			WebSitemapUrl url = new WebSitemapUrl.Options(urlItem).lastMod(Calendar.getInstance().getTime())
					.priority(0.4).changeFreq(ChangeFreq.MONTHLY).build();
			sitemapGenerator.addUrl(url);
		}
		userItemReader.close();

		sitemapGenerator.write();
		sitemapGenerator.writeSitemapsWithIndex();

		logger.info("Done sitemap update...");

		System.gc(); // Tip

	}

	public WebSitemapGenerator sitemapGenerator(Environment environment) throws Exception {
		return WebSitemapGenerator.builder(Constant.URL_ROOT_PATH, new File(Constant.SITEMAP_XML_LOCAL_PATH))
				.allowMultipleSitemaps(true).gzip(true).maxUrls(1000).build();
	}

}
