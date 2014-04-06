package by.gravity.expensemanager.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import by.gravity.expensemanager.data.SettingsManager;

public class SQLDataManagerHelper extends SQLiteOpenHelper {

	public static final String DATA_BASE_NAME = "expenceManager.db";

	private static final int DATABASE_VERSION = 1;

	public SQLDataManagerHelper(Context context) {
		super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
	}
	
	private static final String CREATE_TABLE_EXPENCE = "CREATE TABLE IF NOT EXISTS "
			+ SQLConstants.TABLE_EXPENSE
			+ "("
			+ SQLConstants.FIELD_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ SQLConstants.FIELD_AMOUNT
			+ " VARCHAR (50),"
			+ SQLConstants.FIELD_CURRENCY
			+ " INTEGER (20),"
			+ SQLConstants.FIELD_DATE
			+ " INTEGER,"
			+ SQLConstants.FIELD_TIME
			+ " INTEGER,"
			+ SQLConstants.FIELD_NOTE
			+ " VARCHAR (200),"
			+ SQLConstants.FIELD_PAYMENT_METHOD
			+ " INTEGER (20));";
	
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS "
			+ SQLConstants.TABLE_CATEGORY
			+ "("
			+ SQLConstants.FIELD_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ SQLConstants.FIELD_NAME
			+ " VARCHAR (50),"
			+ SQLConstants.FIELD_USAGE_COUNT
			+ " INTEGER DEFAULT 0);";
	
	private static final String CREATE_TABLE_EXPENCE_CATEGORY = "CREATE TABLE IF NOT EXISTS "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY
			+ "("
			+ SQLConstants.FIELD_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ SQLConstants.FIELD_EXPENSE_ID
			+ " INTEGER (20)  NOT NULL,"
			+ SQLConstants.FIELD_CATEGORY_ID
			+ " INTEGER (20)  NOT NULL);";
	
	private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE IF NOT EXISTS "
			+ SQLConstants.TABLE_CURRENCY
			+ "("
			+ SQLConstants.FIELD_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ SQLConstants.FIELD_CODE
			+ " VARCHAR (10),"
			+ SQLConstants.FIELD_NAME
			+ " VARCHAR (100),"
			+ SQLConstants.FIELD_NAME_EN
			+ " VARCHAR (100),"
			+ SQLConstants.FIELD_IS_USED
			+ " INTEGER,"
			+ SQLConstants.FIELD_RATE
			+ " DOUBLE DEFAULT 1);";

	private static final String CREATE_TABLE_PAYMENT_METHODS = "CREATE TABLE IF NOT EXISTS "
			+ SQLConstants.TABLE_PAYMENT_METHODS
			+ "("
			+ SQLConstants.FIELD_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ SQLConstants.FIELD_NAME
			+ " VARCHAR (100),"
			+ SQLConstants.FIELD_NOTE
			+ " VARCHAR (200),"
			+ SQLConstants.FIELD_BALANCE
			+ " VARCHAR (50),"
			+ SQLConstants.FIELD_CURRENCY
			+ " INTEGER );";
	
	
	private static final String ADD_CURRENCY_VALUES = "INSERT INTO "+SQLConstants.TABLE_CURRENCY +
		"("+SQLConstants.FIELD_NAME + "," + SQLConstants.FIELD_NAME_EN +","+SQLConstants.FIELD_CODE+","+SQLConstants.FIELD_RATE+")"+
		" SELECT 'Афгани' AS " + SQLConstants.FIELD_NAME + ", 'Afghani' AS " + SQLConstants.FIELD_NAME_EN +
		", 'AFN' AS " + SQLConstants.FIELD_CODE + ", '56.6' AS " + SQLConstants.FIELD_RATE+ 
		" UNION SELECT 'Алжирский динар', 'Algerian Dinar', 'DZD', '79.145'"+
		" UNION SELECT 'Армянский драм', 'Armenian Dram', 'AMD', '415.13'"+
		" UNION SELECT 'Арубанский флорин', 'Aruban Guilder', 'AWG', '1.785'"+
		" UNION SELECT 'Австралийский доллар', 'Australian Dollar', 'AUD', '1.0765'"+
		" UNION SELECT 'Азербайджанский манат', 'Azerbaijanian Manat', 'AZN', '0.7844'"+
		" UNION SELECT 'Багамский доллар', 'Bahamian Dollar', 'BSD', '1.0'"+
		" UNION SELECT 'Бахрейнский динар', 'Bahraini Dinar', 'BHD', '0.3771'"+
		" UNION SELECT 'Бат', 'Baht', 'THB', '32.465'"+
		" UNION SELECT 'Бальбоа', 'Balboa', 'PAB', '1.0'"+
		" UNION SELECT 'Барбадосский доллар', 'Barbados Dollar', 'BBD', '2.0'"+
		" UNION SELECT 'Белорусский рубль', 'Belarussian Ruble', 'BYR', '9910'"+
		" UNION SELECT 'Белизский доллар', 'Belize Dollar', 'BZD', '2.015'"+
		" UNION SELECT 'Бермудский доллар', 'Bermudian Dollar', 'BMD', '1.0'"+
		" UNION SELECT 'Боливар фуэрте', 'Bolivar', 'VEF', '6.2937'"+
		" UNION SELECT 'Боливиано', 'Boliviano', 'BOB', '6.91'"+
		" UNION SELECT 'Бразильский реал', 'Brazilian Real', 'BRL', '2.242'"+
		" UNION SELECT 'Брунейский доллар', 'Brunei Dollar', 'BGN', '1.4326'"+
		" UNION SELECT 'Бурундийский франк', 'Burundi Franc', 'BIF', '1538.0'"+
		" UNION SELECT 'Канадский доллар', 'Canadian Dollar', 'CAD', '1.098'"+
		" UNION SELECT 'Эксудо Кабо-Верде', 'Cape Verder Escudo', 'CVE', '79.83'"+
		" UNION SELECT 'Доллар Островов Кайман ', 'Cayman Islands Dollar', 'KYD', '0.82'"+
		" UNION SELECT 'Франк КФА ВСЕАО', 'CFA Franc BCEAO', 'XOF', '488.4'"+
		" UNION SELECT 'Франк КФА ВЕАС', 'CFA Franc BEAF', 'XAF', '478.6784'"+
		" UNION SELECT 'Франк КПФ', 'CFЗ Franc', 'XPF', '87.29'"+
		" UNION SELECT 'Чилийское песо', 'Chilean Peso', 'CLP', '555.28'"+
		" UNION SELECT 'Колумбийское песо', 'Columbian Peso', 'COP', '1951.7'"+
		" UNION SELECT 'Франк Комор', 'Comoro Franc', 'KMF', '359.0088'"+
		" UNION SELECT 'Конголезский франк', 'Congolese Franc', 'CDF', '924.0'"+
		" UNION SELECT 'Конфертируемая марка', 'Convertible Mark', 'BAM', '1.4303'"+
		" UNION SELECT 'Золотая кордоба', 'Corboda Oro', 'NIO', '25.885'"+
		" UNION SELECT 'Костариканский колон', 'Costa Rican Colon', 'CRC', '551.95'"+
		" UNION SELECT 'Хорватская куна', 'Croatian Kuna', 'HRK', '5.5728'"+
		" UNION SELECT 'Кубинское песо', 'Cuban Peso', 'CUP', '1.0'"+
		" UNION SELECT 'Чешская крона', 'Czech Koruna', 'CZK', '20.0234'"+
		" UNION SELECT 'Даласи', 'Dalasi', 'GMD', '38.1'"+
		" UNION SELECT 'Датская крона', 'Danish Krone', 'DKK', '5.4482'"+
		" UNION SELECT 'Денар', 'Denar', 'MKD', '45.0'"+
		" UNION SELECT 'Франк Джибути', 'Djibouti Franc', 'DJF', '179.0'"+
		" UNION SELECT 'Добра', 'Dobra', 'STD', '17927.0'"+
		" UNION SELECT 'Доминиканское песо', 'Dominican Peso', 'DOP', '43.13'"+
		" UNION SELECT 'Донг', 'Dong', 'VND', '21084.0'"+
		" UNION SELECT 'Восточно-карибский доллар', 'East Caribbean Dolar', 'XCD', '2.7'"+
		" UNION SELECT 'Египетский фунт', 'Egyptian Pound', 'EGP', '6.9724'"+
		" UNION SELECT 'Сальвадорский колон', 'EL Salvador Colon', 'SVC', '8.747'"+
		" UNION SELECT 'Эфиорский быр', 'Ethiopian Birr', 'ETB', '19.4135'"+
		" UNION SELECT 'Евро', 'Euro', 'EUR', '0.7297'"+
		" UNION SELECT 'Фунт Фолклендских островов', 'Falkland Islands Pound', 'FKP', '0.6032'"+
		" UNION SELECT 'Доллар Фиджи', 'Fiji Dollar', 'FJD', '1.855'"+
		" UNION SELECT 'Форинт', 'Forint', 'HUF', '223.08'"+
		" UNION SELECT 'Ганский седи', 'Ghana Cedi', 'GHS', '2.6988'"+
		" UNION SELECT 'Гибралтарский фунт', 'Gibraltar Pound', 'GIP', '0.6032'"+
		" UNION SELECT 'Гурд', 'Gourde', 'HTG', '44.7623'"+
		" UNION SELECT 'Гуарани', 'Guarani', 'PYG', '4456.21'"+
		" UNION SELECT 'Гвинейский франк', 'Guinea Franc', 'GNF', '7020.0'"+
		" UNION SELECT 'Гайанский доллар', 'Guyana Dollar', 'GYD', '205.7'"+
		" UNION SELECT 'Гонконгский доллар', 'Hong Kong Dollar', 'HKD', '7.7568'"+
		" UNION SELECT 'Гривна', 'Hryvnia', 'UAH', '11.606'"+
		" UNION SELECT 'Исландская крона', 'Iceland Krona', 'ISK', '112.65'"+
		" UNION SELECT 'Индийская рупия', 'Indian Rupee', 'INR', '60.095'"+
		" UNION SELECT 'Иранский риал', 'Iranian Rial', 'IRR', '25469'"+
		" UNION SELECT 'Иракский динар', 'Iraqi Dinar', 'IQD', '1164.4'"+
		" UNION SELECT 'Ямайский доллар', 'Jamaican Dollar', 'JMD', '109.27'"+
		" UNION SELECT 'Иорданский динар', 'Jordanian Dinar', 'JOD', '0.7085'"+
		" UNION SELECT 'Кенийский шиллинг', 'Kenyan Shilling', 'KES', '86.7'"+
		" UNION SELECT 'Кина', 'Kina', 'PGK', '2.7601'"+
		" UNION SELECT 'Кип', 'Kip', 'LAK', '8042.7998'"+
		" UNION SELECT 'Кувейтский динар', 'Kiwaiti Dinar', 'KWD', '0.2824'"+
		" UNION SELECT 'Квача', 'Kwacha', 'MWK', '412.5'"+
		" UNION SELECT 'Кванза', 'Kwanza', 'AOA', '97.73'"+
		" UNION SELECT 'Кьят', 'Kyat', 'MMK', '964.0'"+
		" UNION SELECT 'Лари', 'Lari', 'GEL', '1.7461'"+
		" UNION SELECT 'Ливанский фунт', 'Lebanese Pound', 'LBP', '1507.5'"+
		" UNION SELECT 'Лек', 'Lek', 'ALL', '102.1'"+
		" UNION SELECT 'Лемпира', 'Lempira', 'HNL', '19.22'"+
		" UNION SELECT 'Леоне', 'Leone', 'SLL', '4333.0'"+
		" UNION SELECT 'Либерийский доллар', 'Liberian Dollar', 'LRD', '85.5'"+
		" UNION SELECT 'Ливийский динар', 'Libyan Dinar', 'LYD', '1.245'"+
		" UNION SELECT 'Лилангени', 'Lilangeni', 'SZL', '10.5485'"+
		" UNION SELECT 'Лит', 'Lithuanian Litas', 'LTL', '2.521'"+
		" UNION SELECT 'Лоти', 'Loti', 'LSL', '10.565'"+
		" UNION SELECT 'Малагасийский ариари', 'Malagasy Ariary', 'MGA', '2354.0'"+
		" UNION SELECT 'Малайзийский ринггит', 'Malaysian Ringgit', 'MYR', '3.2795'"+
		" UNION SELECT 'Маврикийская рупия', 'Mauritius Rupee', 'MUR', '30.31'"+
		" UNION SELECT 'Мексиканское песо', 'Mexican Peso', 'MXN', '13.0085'"+
		" UNION SELECT 'Молдавский лей', 'Moldavan Leu', 'MDL', '13.19'"+
		" UNION SELECT 'Марокканский дирхам', 'Moroccan Dirham', 'MAD', '8.1941'"+
		" UNION SELECT 'Мозамбикский метикал', 'Mozambique Metical', 'MXN', '13.0085'"+
		" UNION SELECT 'Найра', 'Naira', 'NGN', '164.0'"+
		" UNION SELECT 'Накфа', 'Nakfa', 'ERN', '15.1'"+
		" UNION SELECT 'Доллар Намибии', 'Namibia Dollar', 'NAD', '10.55'"+
		" UNION SELECT 'Непальская рупия', 'Nepalese Rupee', 'NPR', '96.91'"+
		" UNION SELECT 'Нидерландский антильский гульден', 'Netherlands Antiilean Guilder', 'ANG', '1.79'"+
		" UNION SELECT 'Новый израильский шекел', 'New Israeli Sheqel', 'ILS', '3.4839'"+
		" UNION SELECT 'Новый тайваньский доллар', 'New Taiwan Dollar', 'TWD', '30.224'"+
		" UNION SELECT 'Новозеландский доллар', 'New Zealand Dollar', 'NZD', '1.1633'"+
		" UNION SELECT 'Нгултрум', 'Ngultrum', 'BTN', '60.185'"+
		" UNION SELECT 'Северокорейская вона', 'North Korean Won', 'KPW', '900.0'"+
		" UNION SELECT 'Норвежская крона', 'Norwegian Krone', 'NOK', '6.001'"+
		" UNION SELECT 'Новый соль', 'Nuevo Sol', 'PEN', '2.807'"+
		" UNION SELECT 'Угия', 'Ouguiya', 'MRO', '290.95'"+
		" UNION SELECT 'Пакистанская рупия', 'Pakistan Rupee', 'PKR', '98.12'"+
		" UNION SELECT 'Патака', 'Pataca', 'MOP', '7.9896'"+
		" UNION SELECT 'Паанга', 'Paanga', 'TOP', '1.8381'"+
		" UNION SELECT 'Уругвайское песо', 'Peso Uruguayo', 'UYU', '22.92'"+
		" UNION SELECT 'Филиппинское песо', 'Philippine Peso', 'PHP', '44.955'"+
		" UNION SELECT 'Фунт стерлингов', 'Pound Sterling', 'GBP', '0.6033'"+
		" UNION SELECT 'Пула', 'Pula', 'BWP', '8.8261'"+
		" UNION SELECT 'Катарский риал', 'Qatari Rial', 'QAR', '3.6413'"+
		" UNION SELECT 'Кетсаль', 'Quetzal', 'GTQ', '7.7735'"+
		" UNION SELECT 'Рэнд', 'Rand', 'ZAR', '10.5603'"+
		" UNION SELECT 'Оманский риал', 'Rial Omani', 'OMR', '0.3848'"+
		" UNION SELECT 'Риель', 'Riel', 'KHR', '4004.0'"+
		" UNION SELECT 'Новый румынский лей', 'Romanian Leu', 'RON', '3.2543'"+
		" UNION SELECT 'Руфия', 'Rupiah', 'IDR', '11323.0'"+
		" UNION SELECT 'Российский рубль', 'Russian Ruble', 'RUB', '35.3139'"+
		" UNION SELECT 'Франк Руанды', 'RWANDA Franc', 'RWF', '681.0'"+
		" UNION SELECT 'Фунт Святой Елены', 'Saint Helena Pound', 'SHP', '0.6032'"+
		" UNION SELECT 'Саудовский риял', 'Saudi Riyal', 'SAR', '3.7504'"+
		" UNION SELECT 'Сербский динар', 'Serbian Dinar', 'RSD', '84.2705'"+
		" UNION SELECT 'Сингапурский доллар', 'Singapore Dollar', 'SGD', '1.2588'"+
		" UNION SELECT 'Сейшельская рупия', 'Seychelles Rupee', 'SCR', '11.9985'"+
		" UNION SELECT 'Доллар Соломоновых Островов', 'Solomon Islands Dollar', 'SBD', '7.2747'"+
		" UNION SELECT 'Сом', 'Som', 'KGS', '54.489'"+
		" UNION SELECT 'Сомалийский шиллинг', 'Somali Shiling', 'SOS', '1001.5'"+
		" UNION SELECT 'Сомони', 'Somoni', 'TJS', '4.8502'"+
		" UNION SELECT 'Шри-ланкийская рупия', 'Sri Lanka Rupee', 'LKR', '130.6'"+
		" UNION SELECT 'Суданский фунт', 'Sudanese Pound', 'SDG', '5.6863'"+
		" UNION SELECT 'Суринамский доллар', 'Surinam Dollar', 'SRD', '3.2725'"+
		" UNION SELECT 'Шведская крона', 'Swedish Krona', 'SEK', '6.5574'"+
		" UNION SELECT 'Швейцарский франк', 'Swiss Franc', 'CHF', '0.8921'"+
		" UNION SELECT 'Сирийский фунт', 'Syrian Pound', 'SYP', '144.45'"+
		" UNION SELECT 'Така', 'Taka', 'BDT', '77.6'"+
		" UNION SELECT 'Тала', 'Tala', 'WST', '2.3071'"+
		" UNION SELECT 'Танзанийский шиллинг', 'Tanzanian Shiling', 'TZS', '1630.5'"+
		" UNION SELECT 'Тенге', 'Tenge', 'KZT', '182.09'"+
		" UNION SELECT 'Доллар Тринилала и Тобаго', 'Trinidad and Tobago Dollar', 'TTD', '6.4405'"+
		" UNION SELECT 'Тугрик', 'Tugrik', 'MNT', '1777.5'"+
		" UNION SELECT 'Тунисскйи динар', 'Tunisian Dinar', 'TND', '1.5908'"+
		" UNION SELECT 'Турецкая лира', 'Turkish Lira', 'TRY', '2.1135'"+
		" UNION SELECT 'Новый туркменский манат', 'Turkmenistan New Manat', 'TMT', '2.85'"+
		" UNION SELECT 'Дирхам ОАЭ', 'UAE Dirham', 'AED', '3.6731'"+
		" UNION SELECT 'Угандийский шиллинг', 'Uganda Shiling', 'UGX', '2563.0'"+
		" UNION SELECT 'Доллар США', 'US Dollar', 'USD', '1.0'"+
		" UNION SELECT 'Узбекский сум', 'Uzbekistan Sum', 'UZS', '2262.9299'"+
		" UNION SELECT 'Вату', 'Vatu', 'VUV', '94.52'"+
		" UNION SELECT 'Вона', 'Won', 'KRW', '1052.96'"+
		" UNION SELECT 'Йеменский риал', 'Yemeni Rial', 'YER', '214.925'"+
		" UNION SELECT 'Иена', 'Yen', 'JPY', '103.285'"+
		" UNION SELECT 'Юань', 'Yuan Renminbi', 'CNY', '6.2112'"+
		" UNION SELECT 'Замбийская квача', 'Zambian Kwacha', 'ZMW', '6.15'"+
		" UNION SELECT 'Доллар Зимбабве', 'Zimbabwe Dollar', 'ZWL', '322.355'"+
		" UNION SELECT 'Злотый', 'Zloty', 'PLN', '3.0387'";
	
	private static final String DEFAULT_USED_CURRENCIES = "UPDATE " + SQLConstants.TABLE_CURRENCY + 
		" SET "+SQLConstants.FIELD_IS_USED + " = 1"+
		" WHERE " + SQLConstants.FIELD_CODE + " IN (%s)";

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EXPENCE);
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_CURRENCY);
		db.execSQL(CREATE_TABLE_EXPENCE_CATEGORY);
		db.execSQL(CREATE_TABLE_PAYMENT_METHODS);
		db.execSQL(ADD_CURRENCY_VALUES);
		db.execSQL(String.format(DEFAULT_USED_CURRENCIES, SettingsManager.getUsedCurrencies(true)));
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
