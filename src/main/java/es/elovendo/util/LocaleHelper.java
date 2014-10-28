package es.elovendo.util;

import java.util.Locale;

import org.apache.log4j.Logger;

import es.elovendo.util.currency.NoFixLocaleFoundException;

public class LocaleHelper {

	Logger logger = Logger.getLogger(LocaleHelper.class);

	private static LocaleHelper localeHelper = null;

	public static LocaleHelper getInstance() {
		if (localeHelper == null) {
			localeHelper = new LocaleHelper();
			return localeHelper;
		}
		return localeHelper;
	}

	/**
	 * Get Locale object, with all attributes
	 * @param locale
	 * @return
	 * @throws NoFixLocaleFoundException If no matching Locale found 
	 */
	public Locale getFixedLocale(Locale locale) throws NoFixLocaleFoundException {
		Locale[] locales = Locale.getAvailableLocales();
		Locale proLocale = null;
		
		for (Locale loc : locales) {
			if (loc.getDisplayLanguage().equals(locale.getDisplayLanguage()) && !loc.getISO3Country().isEmpty()) {
				logger.error("found " + loc.getCountry());
				if (!loc.getCountry().equalsIgnoreCase(locale.toString()) && proLocale != null) {
					logger.error("Found candidate " + loc.getCountry());
					proLocale = loc;
				}
				else return loc;
			}
		}

		logger.warn("Fixing locale: nothing found");
		if (proLocale != null) return proLocale;
		else throw new NoFixLocaleFoundException("No locale found for " + locale);
	}

}
