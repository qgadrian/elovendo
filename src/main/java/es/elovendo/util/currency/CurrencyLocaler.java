package es.elovendo.util.currency;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.elovendo.util.LocaleHelper;

public class CurrencyLocaler {
	
	private static CurrencyLocaler currencyLocaler;
	private static JSONObject jsonCountries;
	private static JSONParser parser;
	private static final String JSON_CURRENCY_FILE = "/home/elovendo/elovendo/currency.json";
	
	private CurrencyLocaler() {}
	
	public static CurrencyLocaler getInstance() {
		if (currencyLocaler == null) currencyLocaler = new CurrencyLocaler();
		return currencyLocaler;
	}
	
	private void initCountryJSON() throws FileNotFoundException, IOException, ParseException {
		parser = new JSONParser();
		jsonCountries = ((JSONObject) parser.parse(new FileReader(JSON_CURRENCY_FILE)));
	}
	
	public Currency getCurrencyLocaled(Locale locale) throws NoCurrencyLocaleException {
		Currency fromCurrency = null;
		try {
			fromCurrency = Currency.getInstance(locale);
		} catch (IllegalArgumentException | NullPointerException e) {
			
			// Fix locale without country specified
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			try {
				locale = localeHelper.getFixedLocale(locale);
				fromCurrency = Currency.getInstance(locale);
			} catch (NoFixLocaleFoundException e1) {
				throw new NoCurrencyLocaleException();
			}
		}
		
		return fromCurrency;
	}
	
	public Locale getLocaleFromCurrency(Currency currency) {
		return null;
	}
	
	/**
	 * Get an well format, based on locale, amount
	 * @param amount Amount to exchange
	 * @param locale Locale to format
	 * @param Currency Amount current currency
	 * @return
	 * @throws CurrencyConvertException
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getFormattedCurrency(BigDecimal amount, Locale locale, Currency currency) {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
		numberFormat.setCurrency(currency);	
		return numberFormat.format(amount);
	}
	
	/**
	 * Get an well format, based on locale, exchanged value
	 * @param amount Amount to exchange
	 * @param locale Locale to format
	 * @param fromCurrency Original's currency
	 * @param toCurrency Target Currency to format
	 * @return Target currency formatted by locale
	 * @throws CurrencyConvertException
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getFormattedCurrency(BigDecimal amount, Locale locale, Currency fromCurrency, Currency toCurrency)
			throws CurrencyConvertException, ParseException, FileNotFoundException, IOException {
		// Exchange from item currency to locale currency
		if (!fromCurrency.getCurrencyCode().equalsIgnoreCase(toCurrency.getCurrencyCode())) {
			CurrencyConverter converter = CurrencyConverter.getInstance();
			BigDecimal exchanged = converter.convert(amount, fromCurrency, toCurrency);
			// Format using localeCurrency
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			numberFormat.setCurrency(toCurrency);
			return numberFormat.format(exchanged);
		}
		// If item's currency and locale currency is the same, just format the
		// prize
		else {
			// Format using fromCurrency (actual currency)
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			numberFormat.setCurrency(fromCurrency);
			return numberFormat.format(amount);
		}
	}

}
