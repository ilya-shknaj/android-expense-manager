package by.gravity.expensemanager.data.helper;

import by.gravity.expensemanager.data.SettingsManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
			+ " INTEGER);";

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
		"("+SQLConstants.FIELD_NAME + "," + SQLConstants.FIELD_NAME_EN +","+SQLConstants.FIELD_CODE+")"+
		" SELECT 'Афгани' AS name, 'Afghani' AS name_en, 'AFN' as code"+
		" UNION SELECT 'Алжирский динар', 'Algerian Dinar', 'DZD'"+
		" UNION SELECT 'Армянский драм', 'Armenian Dram', 'AMD'"+
		" UNION SELECT 'Арубанский флорин', 'Aruban Guilder', 'AWG'"+
		" UNION SELECT 'Австралийский доллар', 'Australian Dollar', 'AUD'"+
		" UNION SELECT 'Азербайджанский манат', 'Azerbaijanian Manat', 'AZN'"+
		" UNION SELECT 'Багамский доллар', 'Bahamian Dollar', 'BSD'"+
		" UNION SELECT 'Бахрейнский динар', 'Bahraini Dinar', 'BHD'"+
		" UNION SELECT 'Бат', 'Baht', 'THB'"+
		" UNION SELECT 'Бальбоа', 'Balboa', 'PAB'"+
		" UNION SELECT 'Барбадосский доллар', 'Barbados Dollar', 'BBD'"+
		" UNION SELECT 'Белорусский рубль', 'Belarussian Ruble', 'BYR'"+
		" UNION SELECT 'Белизский доллар', 'Belize Dollar', 'BZD'"+
		" UNION SELECT 'Бермудский доллар', 'Bermudian Dollar', 'BMD'"+
		" UNION SELECT 'Боливар фуэрте', 'Bolivar', 'VEF'"+
		" UNION SELECT 'Боливиано', 'Boliviano', 'BOB'"+
		" UNION SELECT 'Бразильский реал', 'Brazilian Real', 'BRL'"+
		" UNION SELECT 'Брунейский доллар', 'Brunei Dollar', 'BGN'"+
		" UNION SELECT 'Бурундийский франк', 'Burundi Franc', 'BIF'"+
		" UNION SELECT 'Канадский доллар', 'Canadian Dollar', 'CAD'"+
		" UNION SELECT 'Эксудо Кабо-Верде', 'Cape Verder Escudo', 'CVE'"+
		" UNION SELECT 'Доллар Островов Кайман ', 'Cayman Islands Dollar', 'KYD'"+
		" UNION SELECT 'Франк КФА ВСЕАО', 'CFA Franc BCEAO', 'XOF'"+
		" UNION SELECT 'Франк КФА ВЕАС', 'CFA Franc BEAF', 'XAF'"+
		" UNION SELECT 'Франк КПФ', 'CFЗ Franc', 'XPF'"+
		" UNION SELECT 'Чилийское песо', 'Chilean Peso', 'CLP'"+
		" UNION SELECT 'Колумбийское песо', 'Columbian Peso', 'COP'"+
		" UNION SELECT 'Франк Комор', 'Comoro Franc', 'KMF'"+
		" UNION SELECT 'Конголезский франк', 'Congolese Franc', 'CDF'"+
		" UNION SELECT 'Конфертируемая марка', 'Convertible Mark', 'BAM'"+
		" UNION SELECT 'Золотая кордоба', 'Corboda Oro', 'NIO'"+
		" UNION SELECT 'Костариканский колон', 'Costa Rican Colon', 'CRC'"+
		" UNION SELECT 'Хорватская куна', 'Croatian Kuna', 'HRK'"+
		" UNION SELECT 'Кубинское песо', 'Cuban Peso', 'CUP'"+
		" UNION SELECT 'Чешская крона', 'Czech Koruna', 'CZK'"+
		" UNION SELECT 'Даласи', 'Dalasi', 'GMD'"+
		" UNION SELECT 'Датская крона', 'Danish Krone', 'DKK'"+
		" UNION SELECT 'Денар', 'Denar', 'MKD'"+
		" UNION SELECT 'Франк Джибути', 'Djibouti Franc', 'DJF'"+
		" UNION SELECT 'Добра', 'Dobra', 'STD'"+
		" UNION SELECT 'Доминиканское песо', 'Dominican Peso', 'DOP'"+
		" UNION SELECT 'Донг', 'Dong', 'VND'"+
		" UNION SELECT 'Восточно-карибский доллар', 'East Caribbean Dolar', 'XCD'"+
		" UNION SELECT 'Египетский фунт', 'Egyptian Pound', 'EGP'"+
		" UNION SELECT 'Сальвадорский колон', 'EL Salvador Colon', 'SVC'"+
		" UNION SELECT 'Эфиорский быр', 'Ethiopian Birr', 'ETB'"+
		" UNION SELECT 'Евро', 'Euro', 'EUR'"+
		" UNION SELECT 'Фунт Фолклендских островов', 'Falkland Islands Pound', 'FKP'"+
		" UNION SELECT 'Доллар Фиджи', 'Fiji Dollar', 'FJD'"+
		" UNION SELECT 'Форинт', 'Forint', 'HUF'"+
		" UNION SELECT 'Ганский седи', 'Ghana Cedi', 'GHS'"+
		" UNION SELECT 'Гибралтарский фунт', 'Gibraltar Pound', 'GIP'"+
		" UNION SELECT 'Гурд', 'Gourde', 'HTG'"+
		" UNION SELECT 'Гуарани', 'Guarani', 'PYG'"+
		" UNION SELECT 'Гвинейский франк', 'Guinea Franc', 'GNF'"+
		" UNION SELECT 'Гайанский доллар', 'Guyana Dollar', 'GYD'"+
		" UNION SELECT 'Гонконгский доллар', 'Hong Kong Dollar', 'HKD'"+
		" UNION SELECT 'Гривна', 'Hryvnia', 'UAH'"+
		" UNION SELECT 'Исландская крона', 'Iceland Krona', 'ISK'"+
		" UNION SELECT 'Индийская рупия', 'Indian Rupee', 'INR'"+
		" UNION SELECT 'Иранский риал', 'Iranian Rial', 'IRR'"+
		" UNION SELECT 'Иракский динар', 'Iraqi Dinar', 'IQD'"+
		" UNION SELECT 'Ямайский доллар', 'Jamaican Dollar', 'JMD'"+
		" UNION SELECT 'Иорданский динар', 'Jordanian Dinar', 'JOD'"+
		" UNION SELECT 'Кенийский шиллинг', 'Kenyan Shilling', 'KES'"+
		" UNION SELECT 'Кина', 'Kina', 'PGK'"+
		" UNION SELECT 'Кип', 'Kip', 'LAK'"+
		" UNION SELECT 'Кувейтский динар', 'Kiwaiti Dinar', 'KWD'"+
		" UNION SELECT 'Квача', 'Kwacha', 'MWK'"+
		" UNION SELECT 'Кванза', 'Kwanza', 'AOA'"+
		" UNION SELECT 'Кьят', 'Kyat', 'MMK'"+
		" UNION SELECT 'Лари', 'Lari', 'GEL'"+
		" UNION SELECT 'Ливанский фунт', 'Lebanese Pound', 'LBP'"+
		" UNION SELECT 'Лек', 'Lek', 'ALL'"+
		" UNION SELECT 'Лемпира', 'Lempira', 'HNL'"+
		" UNION SELECT 'Леоне', 'Leone', 'SLL'"+
		" UNION SELECT 'Либерийский доллар', 'Liberian Dollar', 'LRD'"+
		" UNION SELECT 'Ливийский динар', 'Libyan Dinar', 'LYD'"+
		" UNION SELECT 'Лилангени', 'Lilangeni', 'SZL'"+
		" UNION SELECT 'Лит', 'Lithuanian Litas', 'LTL'"+
		" UNION SELECT 'Лоти', 'Loti', 'LSL'"+
		" UNION SELECT 'Малагасийский ариари', 'Malagasy Ariary', 'MGA'"+
		" UNION SELECT 'Малайзийский ринггит', 'Malaysian Ringgit', 'MYR'"+
		" UNION SELECT 'Маврикийская рупия', 'Mauritius Rupee', 'MUR'"+
		" UNION SELECT 'Мексиканское песо', 'Mexican Peso', 'MXN'"+
		" UNION SELECT 'Молдавский лей', 'Moldavan Leu', 'MDL'"+
		" UNION SELECT 'Марокканский дирхам', 'Moroccan Dirham', 'MAD'"+
		" UNION SELECT 'Мозамбикский метикал', 'Mozambique Metical', 'MXN'"+
		" UNION SELECT 'Найра', 'Naira', 'NGN'"+
		" UNION SELECT 'Накфа', 'Nakfa', 'ERN'"+
		" UNION SELECT 'Доллар Намибии', 'Namibia Dollar', 'NAD'"+
		" UNION SELECT 'Непальская рупия', 'Nepalese Rupee', 'NPR'"+
		" UNION SELECT 'Нидерландский антильский гульден', 'Netherlands Antiilean Guilder', 'ФТП'"+
		" UNION SELECT 'Новый израильский шекел', 'New Israeli Sheqel', 'ISL'"+
		" UNION SELECT 'Новый тайваньский доллар', 'New Taiwan Dollar', 'TWD'"+
		" UNION SELECT 'Новозеландский доллар', 'New Zealand Dollar', 'NZD'"+
		" UNION SELECT 'Нгултрум', 'Ngultrum', 'BTN'"+
		" UNION SELECT 'Северокорейская вона', 'North Korean Won', 'KPW'"+
		" UNION SELECT 'Норвежская крона', 'Norwegian Krone', 'NOK'"+
		" UNION SELECT 'Новый соль', 'Nuevo Sol', 'PEN'"+
		" UNION SELECT 'Угия', 'Ouguiya', 'MRO'"+
		" UNION SELECT 'Пакистанская рупия', 'Pakistan Rupee', 'PKR'"+
		" UNION SELECT 'Патака', 'Pataca', 'MOP'"+
		" UNION SELECT 'Паанга', 'Paanga', 'TOP'"+
		" UNION SELECT 'Уругвайское песо', 'Peso Uruguayo', 'UYU'"+
		" UNION SELECT 'Филиппинское песо', 'Philippine Peso', 'PHP'"+
		" UNION SELECT 'Фунт стерлингов', 'Pound Sterling', 'GBP'"+
		" UNION SELECT 'Пула', 'Pula', 'BWP'"+
		" UNION SELECT 'Катарский риал', 'Qatari Rial', 'QAR'"+
		" UNION SELECT 'Кетсаль', 'Quetzal', 'GTQ'"+
		" UNION SELECT 'Рэнд', 'Rand', 'ZAR'"+
		" UNION SELECT 'Оманский риал', 'Rial Omani', 'OMR'"+
		" UNION SELECT 'Риель', 'Riel', 'KHR'"+
		" UNION SELECT 'Новый румынский лей', 'Romanian Leu', 'RON'"+
		" UNION SELECT 'Руфия', 'Rupiah', 'IDR'"+
		" UNION SELECT 'Российский рубль', 'Russian Ruble', 'RUB'"+
		" UNION SELECT 'Франк Руанды', 'RWANDA Franc', 'RWF'"+
		" UNION SELECT 'Фунт Святой Елены', 'Saint Helena Pound', 'SHP'"+
		" UNION SELECT 'Саудовский риял', 'Saudi Riyal', 'SAR'"+
		" UNION SELECT 'Сербский динар', 'Serbian Dinar', 'RSD'"+
		" UNION SELECT 'Сейшельская рупия', 'Seychelles Rupee', 'SRC'"+
		" UNION SELECT 'Сингапурский доллар', 'Singapore Dollar', 'SGD'"+
		" UNION SELECT 'Сейшельская рупия', 'Seychelles Rupee', 'SCR'"+
		" UNION SELECT 'Доллар Соломоновых Островов', 'Solomon Islands Dollar', 'SBD'"+
		" UNION SELECT 'Сом', 'Som', 'KGS'"+
		" UNION SELECT 'Сомалийский шиллинг', 'Somali Shiling', 'SOS'"+
		" UNION SELECT 'Сомони', 'Somoni', 'TJS'"+
		" UNION SELECT 'Южносуданский фунт', 'South Sudanese Pound', 'SSP'"+
		" UNION SELECT 'Шри-ланкийская рупия', 'Sri Lanka Rupee', 'LKR'"+
		" UNION SELECT 'Суданский фунт', 'Sudanese Pound', 'SDG'"+
		" UNION SELECT 'Суринамский доллар', 'Surinam Dollar', 'SRD'"+
		" UNION SELECT 'Шведская крона', 'Swedish Krona', 'SEK'"+
		" UNION SELECT 'Швейцарский франк', 'Swiss Franc', 'CHF'"+
		" UNION SELECT 'Сирийский фунт', 'Syrian Pound', 'SYP'"+
		" UNION SELECT 'Така', 'Taka', 'BDT'"+
		" UNION SELECT 'Тала', 'Tala', 'WST'"+
		" UNION SELECT 'Танзанийский шиллинг', 'Tanzanian Shiling', 'TZS'"+
		" UNION SELECT 'Тенге', 'Tenge', 'KZT'"+
		" UNION SELECT 'Доллар Тринилала и Тобаго', 'Trinidad and Tobago Dollar', 'TTD'"+
		" UNION SELECT 'Тугрик', 'Tugrik', 'MNT'"+
		" UNION SELECT 'Тунисскйи динар', 'Tunisian Dinar', 'TND'"+
		" UNION SELECT 'Турецкая лира', 'Turkish Lira', 'TRY'"+
		" UNION SELECT 'Новый туркменский манат', 'Turkmenistan New Manat', 'TMT'"+
		" UNION SELECT 'Дирхам ОАЭ', 'UAE Dirham', 'AED'"+
		" UNION SELECT 'Угандийский шиллинг', 'Uganda Shiling', 'UGX'"+
		" UNION SELECT 'Доллар США', 'US Dollar', 'USD'"+
		" UNION SELECT 'Узбекский сум', 'Uzbekistan Sum', 'UZS'"+
		" UNION SELECT 'Вату', 'Vatu', 'VUV'"+
		" UNION SELECT 'Вона', 'Won', 'KRW'"+
		" UNION SELECT 'Йеменский риал', 'Yemeni Rial', 'YER'"+
		" UNION SELECT 'Иена', 'Yen', 'JPY'"+
		" UNION SELECT 'Юань', 'Yuan Renminbi', 'CNY'"+
		" UNION SELECT 'Замбийская квача', 'Zambian Kwacha', 'ZMW'"+
		" UNION SELECT 'Доллар Зимбабве', 'Zimbabwe Dollar', 'ZWL'"+
		" UNION SELECT 'Злотый', 'Zloty', 'PLN'";
	
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
