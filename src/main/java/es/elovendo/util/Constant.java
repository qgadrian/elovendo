package es.elovendo.util;

public class Constant {
	
	public static final String ROLE_USER = "ROLE_USER";
	
	public static final int MIN_ITEM_TITLE_LENGHT = 4;
	
	public static final int DEFAULT_USER_VALUE = 70;
	public static final int REP_LIMIT_USER_VALUE = 80;
	
	public static final int INITIAL_POINTS = 0;
	
	public static final int ITEM_DEFAULT_DURATION = 30;
	public static final int DEFAULT_RENEW_DAYS = 30;
	public static final int MAX_RANDOM_ITEMS = 12;
	public static final int MAX_LAST_FAVORITES = 5;
	public static final int MAX_LAST_ITEMS = 5;
	
	/** Elovendo's email accounts **/
	public static final String CONTACT_EMAIL = "contact@elovendo.com";
	
	/** Items per page **/
	public static final int MAX_PAGE_SIZE = 50;
	public static final int ITEMS_PER_PAGE = 20;
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
	public static final String URL_ROOT_PATH = "http://www.elovendo.com";
	public static final String SITEMAP_XML_LOCAL_PATH = "./src/main/webapp/resources/sitemap";
	
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
	public static final String LOGIN_PATTERN = "^[a-zA-ZÑñ]{2,20}$";
	// Strong password
//	public static final String PASSWORD_PATTERN = "^$|(?=^.{8,255}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
	// Weak password
	public static final String PASSWORD_PATTERN = "^$|[0-9a-zA-Z$@$!%*#?&]{8,}$";
	public static final String YOUTUBE_URL_PATTERN = "^$|((http|https)\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$";
	
	/**
	 * IMAGES
	 */
	
//	public static final String RESOURCE_IMAGES_PATH = "file:/home/elovendo/elovendo/imgs/";
	public static final String RESOURCE_IMAGES_PATH = "file:/home/adrian/Proyectos/eclipse/elovendo/imgs/";
	public static final String AVATAR_IMAGES_PATH = "imgs/avatars/";
	public static final String AVATAR_DEFAULT = "images/default/profile/default";
	public static final String ITEM_IMG_DEFAULT = "images/default/item/no-image";
	
	/**
	 * VOTE
	 */
	
	// Base value for calculate value of a vote
	public static final int VOTE_BASE = 30;
	
	// Vote positive, negative
	public static final int VOTE_POSITIVE = 1;
	public static final int VOTE_NEGATIVE = 0;
	
	// Vote

	// Vote state & queue
	public static final boolean VOTE_STATE_INACTIVE = false;
	public static final boolean VOTE_STATE_ACTIVE = true;
	public static final boolean VOTE_QUEUED = true;
	public static final boolean VOTE_NOT_QUEUED = false;
	
	/**
	 * PRIZES
	 */
	
	public final static int OP_FEATURE_PRIZE = 3;
	public final static int OP_HIGLIGHT_PRIZE = 2;
	public final static int OP_AUTORENEW_PRIZE = 3;
}
