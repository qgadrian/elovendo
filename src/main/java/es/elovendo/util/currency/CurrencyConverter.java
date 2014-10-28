package es.elovendo.util.currency;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CurrencyConverter {
	
	private final static String JSON_RATES = "rates";
	private static final String JSON_RATE_FILE = "/home/elovendo/elovendo/latest.json";

	Logger logger = Logger.getLogger(CurrencyConverter.class);

	private static CurrencyConverter currencyConverter;
	private static JSONParser parser;
	private static JSONObject jsonRates;

	/**
	 * Creates an instance of {@link CurrencyConverter} and initializes the JSON rates file
	 * @return
	 * @throws FileNotFoundException If JSON rate file cannot be found
	 * @throws IOException
	 * @throws ParseException
	 */
	public static CurrencyConverter getInstance() throws FileNotFoundException, IOException, ParseException {
		if (currencyConverter == null) {
			currencyConverter = new CurrencyConverter();
			parser = new JSONParser();
			jsonRates = (JSONObject) ((JSONObject) parser.parse(new FileReader(JSON_RATE_FILE))).get(JSON_RATES);
		}
		return currencyConverter;
	}
	
	private CurrencyConverter() {}

	/**
	 * Exchanges the given amount from the current currency, to the desired currency as well
	 * @param amount Amount to exchange
	 * @param fromCurrency Amount current currency
	 * @param toCurrency Target currency to exchange
	 * @return {@link BigDecimal} with the current exchanged value
	 * @throws CurrencyConvertException If local JSON file isn't able to get the target rate, and the backup's
	 * API exchange rate provider isn't able too.
	 * @throws ParseException
	 */
	public BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency)
			throws CurrencyConvertException, ParseException {
		try {
			float rate = getLocalCurrencyConvertRate(fromCurrency, toCurrency);
			return amount.divide(new BigDecimal(rate), BigDecimal.ROUND_UP);
			
		} catch (ArithmeticException e) {
			logger.error("JSON rates file is corrupted?");
			try {
				float rate = getCurrencyConvertRate(fromCurrency, toCurrency);
				return amount.divide(new BigDecimal(rate), BigDecimal.ROUND_UP);
			} catch (APICurrencyRateException | NullPointerException | IOException ae) {
				throw new CurrencyConvertException("API exchange error");
			}
			
		}
	}

	/**
	 * Uses YAHOO YQL to get the conversion rate for both currencies
	 * 
	 * @param fromCurrency
	 * @param toCurrency
	 * @return Convert rate for currencyFrom to currencyTo
	 * @throws IOException
	 * @throws APICurrencyRateException 
	 */
	private float getCurrencyConvertRate(Currency fromCurrency, Currency toCurrency) 
			throws IOException, NullPointerException, APICurrencyRateException {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + fromCurrency.getCurrencyCode()
				+ toCurrency.getCurrencyCode() + "=X&f=l1&e=.csv");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpClient.execute(httpGet, responseHandler);

		try {
			return Float.parseFloat(responseBody);
		} catch (NumberFormatException | NullPointerException e) {
			throw new APICurrencyRateException("Error getting convert rate from Yahoo YQL");
		}
	}

	/**
	 * Obtains the current exchange rates from the local JSON file at JSON_RATE_FILE.
	 * @param fromCurrency
	 * @param toCurrency
	 * @return Exchange rate
	 */
	private float getLocalCurrencyConvertRate(Currency fromCurrency, Currency toCurrency) {
		
		float fromRate = ((Number) jsonRates.get(fromCurrency.getCurrencyCode())).floatValue();
		float toRate = ((Number) jsonRates.get(toCurrency.getCurrencyCode())).floatValue();
		
		logger.debug("Obtained *from* rate " + fromRate);
		logger.debug("Obtained *to* rate " + toRate);
		
		return fromRate/toRate;
	}
	
}