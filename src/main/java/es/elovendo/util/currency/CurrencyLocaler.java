package es.elovendo.util.currency;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import es.elovendo.util.LocaleHelper;

public class CurrencyLocaler {
	
	private static Logger logger = Logger.getLogger(CurrencyLocaler.class);
	private static CurrencyLocaler currencyLocaler;
	
	private CurrencyLocaler() {}
	
	public static CurrencyLocaler getInstance() {
		if (currencyLocaler == null) {
			currencyLocaler = new CurrencyLocaler();
			logger = Logger.getLogger(CurrencyLocaler.class); 
		}
		return currencyLocaler;
	}
	
	public Currency getCurrencyLocaled(Locale locale) throws NoCurrencyLocaleException {
		Currency fromCurrency = null;
		try {
			fromCurrency = Currency.getInstance(locale);
		} catch (IllegalArgumentException | NullPointerException e) {
			logger.debug("Error getting currency from lcoale, trying something else...");
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
	
	public String getCurrencyFromLocale(Locale locale, String sCurrency, BigDecimal prize) {
		// Workaround for some language locale's without Country specified
		if (locale.getISO3Country().isEmpty())
			locale = new Locale(locale.toString(), locale.toString());

		try {
			CurrencyLocaler localer = CurrencyLocaler.getInstance();
			Currency currency = Currency.getInstance(sCurrency);
			return localer.getFormattedCurrency(prize, locale, currency);

		} catch (Exception e) {
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			try {
				locale = localeHelper.getFixedLocale(locale);
				CurrencyLocaler localer = CurrencyLocaler.getInstance();
				Currency currency = Currency.getInstance(sCurrency);
				return localer.getFormattedCurrency(prize, locale, currency);
			} catch (NoFixLocaleFoundException e1) {
				return prize.toString();
			}
		}
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
		// If item's currency and locale currency is the same, just format the prize
		else {
			logger.debug("Currencies are the same: " + fromCurrency + " vs " + toCurrency);
			// Format using fromCurrency (actual currency)
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			numberFormat.setCurrency(fromCurrency);
			return numberFormat.format(amount);
		}
	}

}
