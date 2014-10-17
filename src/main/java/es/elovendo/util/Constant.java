package es.elovendo.util;

public class Constant {
	
	public static final int MIN_ITEM_TITLE_LENGHT = 4;
	
	public static final int DEFAULT_USER_VALUE = 70;
	
	public static final int INITIAL_POINTS = 0;
	
	public static final int ITEM_DEFAULT_DURATION = 30;
	public static final int DEFAULT_RENEW_DAYS = 30;
	public static final int MAX_RANDOM_ITEMS = 12;
	public static final int MAX_LAST_FAVORITES = 5;
	public static final int MAX_LAST_ITEMS = 5;
	
	/** Items per page **/
	public static final int ITEMS_PER_PAGE = 15;
	public static final String S_ITEMS_PER_PAGE = "15";
	
	/**
	 * Category - SubCategory
	 */
	public static final String CATEGORY = "category";
	public static final String SUBCATEGORY = "subCategory";
	
	/** DEFAULT RADIUS SEARCH (in KM) */
	public static final double DEFAULT_RADIUS_SEARCH = 5;
	
	/**
	 * URL PATHS
	 */
	
	public static final String ALL_PATH = "all";
	public static final String VOTE_POSITIVE_STRING = "positive";
	public static final String VOTE_NEGATIVE_STRING = "negative";
	public static final String VOTE_PENDING_STRING = "pending";
	
	/**
	 * URL PREFIXES
	 */
	public static final String MOBILE_API_URL_PREFIX_V1 = "/api/zorg/";
	public static final String API_PATTERN = "/api/.*";
	public static final String PAYPAL_PROCESS_PATTERN = "/site/paypalprocess";
	
	/**
	 * IMAGES SUFIXES
	 */
	
	public static final String IMG_SUFFIX_200 = "-200h.jpg";
	public static final String IMG_SUFFIX_JPG = ".jpg";
	
	/**
	 * PATTERNS
	 */
	public static final String LOGIN_PATTERN = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
	public static final String EMAIL_PATTERN="/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$/";
	public static final String PASSWORD_PATTERN = "^$|(?=^.{8,255}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
//	public static final String YOUTUBE_URL_PATTERN = "^$|((http|https)\\:\\/\\/)?(www\\.)?(youtube\\.com/embed/|youtu\\.?be/embed/)\\/.+$";
	public static final String YOUTUBE_URL_PATTERN = "^$|((http|https)\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$";
	
	/**
	 * IMAGES
	 */
	
	public static final String RESOURCE_IMAGES_PATH = "file:/home/adrian/Proyectos/eclipse/elovendo/imgs/";
	
	/**
	 * VOTE
	 */
	
	// Base value for calculate value of a vote
	public static final int VOTE_BASE = 30;
	
	// Vote positive, negative
	public static final int VOTE_POSITIVE = 1;
	public static final int VOTE_NEGATIVE = 0;
	
	// Vote

	// Vote state
	public static final boolean VOTE_INACTIVE = false;
	public static final boolean VOTE_ACTIVE = true;
	
	/**
	 * PRIZES
	 */
	
	public final static int OP_FEATURE_PRIZE = 3;
	public final static int OP_HIGLIGHT_PRIZE = 2;
	public final static int OP_AUTORENEW_PRIZE = 3;
}
