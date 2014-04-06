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
		" SELECT '������' AS " + SQLConstants.FIELD_NAME + ", 'Afghani' AS " + SQLConstants.FIELD_NAME_EN +
		", 'AFN' AS " + SQLConstants.FIELD_CODE + ", '56.6' AS " + SQLConstants.FIELD_RATE+ 
		" UNION SELECT '��������� �����', 'Algerian Dinar', 'DZD', '79.145'"+
		" UNION SELECT '��������� ����', 'Armenian Dram', 'AMD', '415.13'"+
		" UNION SELECT '���������� ������', 'Aruban Guilder', 'AWG', '1.785'"+
		" UNION SELECT '������������� ������', 'Australian Dollar', 'AUD', '1.0765'"+
		" UNION SELECT '��������������� �����', 'Azerbaijanian Manat', 'AZN', '0.7844'"+
		" UNION SELECT '��������� ������', 'Bahamian Dollar', 'BSD', '1.0'"+
		" UNION SELECT '����������� �����', 'Bahraini Dinar', 'BHD', '0.3771'"+
		" UNION SELECT '���', 'Baht', 'THB', '32.465'"+
		" UNION SELECT '�������', 'Balboa', 'PAB', '1.0'"+
		" UNION SELECT '������������ ������', 'Barbados Dollar', 'BBD', '2.0'"+
		" UNION SELECT '����������� �����', 'Belarussian Ruble', 'BYR', '9910'"+
		" UNION SELECT '��������� ������', 'Belize Dollar', 'BZD', '2.015'"+
		" UNION SELECT '���������� ������', 'Bermudian Dollar', 'BMD', '1.0'"+
		" UNION SELECT '������� ������', 'Bolivar', 'VEF', '6.2937'"+
		" UNION SELECT '���������', 'Boliviano', 'BOB', '6.91'"+
		" UNION SELECT '����������� ����', 'Brazilian Real', 'BRL', '2.242'"+
		" UNION SELECT '���������� ������', 'Brunei Dollar', 'BGN', '1.4326'"+
		" UNION SELECT '������������ �����', 'Burundi Franc', 'BIF', '1538.0'"+
		" UNION SELECT '��������� ������', 'Canadian Dollar', 'CAD', '1.098'"+
		" UNION SELECT '������ ����-�����', 'Cape Verder Escudo', 'CVE', '79.83'"+
		" UNION SELECT '������ �������� ������ ', 'Cayman Islands Dollar', 'KYD', '0.82'"+
		" UNION SELECT '����� ��� �����', 'CFA Franc BCEAO', 'XOF', '488.4'"+
		" UNION SELECT '����� ��� ����', 'CFA Franc BEAF', 'XAF', '478.6784'"+
		" UNION SELECT '����� ���', 'CF� Franc', 'XPF', '87.29'"+
		" UNION SELECT '��������� ����', 'Chilean Peso', 'CLP', '555.28'"+
		" UNION SELECT '������������ ����', 'Columbian Peso', 'COP', '1951.7'"+
		" UNION SELECT '����� �����', 'Comoro Franc', 'KMF', '359.0088'"+
		" UNION SELECT '������������ �����', 'Congolese Franc', 'CDF', '924.0'"+
		" UNION SELECT '�������������� �����', 'Convertible Mark', 'BAM', '1.4303'"+
		" UNION SELECT '������� �������', 'Corboda Oro', 'NIO', '25.885'"+
		" UNION SELECT '�������������� �����', 'Costa Rican Colon', 'CRC', '551.95'"+
		" UNION SELECT '���������� ����', 'Croatian Kuna', 'HRK', '5.5728'"+
		" UNION SELECT '��������� ����', 'Cuban Peso', 'CUP', '1.0'"+
		" UNION SELECT '������� �����', 'Czech Koruna', 'CZK', '20.0234'"+
		" UNION SELECT '������', 'Dalasi', 'GMD', '38.1'"+
		" UNION SELECT '������� �����', 'Danish Krone', 'DKK', '5.4482'"+
		" UNION SELECT '�����', 'Denar', 'MKD', '45.0'"+
		" UNION SELECT '����� �������', 'Djibouti Franc', 'DJF', '179.0'"+
		" UNION SELECT '�����', 'Dobra', 'STD', '17927.0'"+
		" UNION SELECT '������������� ����', 'Dominican Peso', 'DOP', '43.13'"+
		" UNION SELECT '����', 'Dong', 'VND', '21084.0'"+
		" UNION SELECT '��������-��������� ������', 'East Caribbean Dolar', 'XCD', '2.7'"+
		" UNION SELECT '���������� ����', 'Egyptian Pound', 'EGP', '6.9724'"+
		" UNION SELECT '������������� �����', 'EL Salvador Colon', 'SVC', '8.747'"+
		" UNION SELECT '��������� ���', 'Ethiopian Birr', 'ETB', '19.4135'"+
		" UNION SELECT '����', 'Euro', 'EUR', '0.7297'"+
		" UNION SELECT '���� ������������ ��������', 'Falkland Islands Pound', 'FKP', '0.6032'"+
		" UNION SELECT '������ �����', 'Fiji Dollar', 'FJD', '1.855'"+
		" UNION SELECT '������', 'Forint', 'HUF', '223.08'"+
		" UNION SELECT '������� ����', 'Ghana Cedi', 'GHS', '2.6988'"+
		" UNION SELECT '������������� ����', 'Gibraltar Pound', 'GIP', '0.6032'"+
		" UNION SELECT '����', 'Gourde', 'HTG', '44.7623'"+
		" UNION SELECT '�������', 'Guarani', 'PYG', '4456.21'"+
		" UNION SELECT '���������� �����', 'Guinea Franc', 'GNF', '7020.0'"+
		" UNION SELECT '��������� ������', 'Guyana Dollar', 'GYD', '205.7'"+
		" UNION SELECT '����������� ������', 'Hong Kong Dollar', 'HKD', '7.7568'"+
		" UNION SELECT '������', 'Hryvnia', 'UAH', '11.606'"+
		" UNION SELECT '���������� �����', 'Iceland Krona', 'ISK', '112.65'"+
		" UNION SELECT '��������� �����', 'Indian Rupee', 'INR', '60.095'"+
		" UNION SELECT '�������� ����', 'Iranian Rial', 'IRR', '25469'"+
		" UNION SELECT '�������� �����', 'Iraqi Dinar', 'IQD', '1164.4'"+
		" UNION SELECT '�������� ������', 'Jamaican Dollar', 'JMD', '109.27'"+
		" UNION SELECT '���������� �����', 'Jordanian Dinar', 'JOD', '0.7085'"+
		" UNION SELECT '��������� �������', 'Kenyan Shilling', 'KES', '86.7'"+
		" UNION SELECT '����', 'Kina', 'PGK', '2.7601'"+
		" UNION SELECT '���', 'Kip', 'LAK', '8042.7998'"+
		" UNION SELECT '���������� �����', 'Kiwaiti Dinar', 'KWD', '0.2824'"+
		" UNION SELECT '�����', 'Kwacha', 'MWK', '412.5'"+
		" UNION SELECT '������', 'Kwanza', 'AOA', '97.73'"+
		" UNION SELECT '����', 'Kyat', 'MMK', '964.0'"+
		" UNION SELECT '����', 'Lari', 'GEL', '1.7461'"+
		" UNION SELECT '��������� ����', 'Lebanese Pound', 'LBP', '1507.5'"+
		" UNION SELECT '���', 'Lek', 'ALL', '102.1'"+
		" UNION SELECT '�������', 'Lempira', 'HNL', '19.22'"+
		" UNION SELECT '�����', 'Leone', 'SLL', '4333.0'"+
		" UNION SELECT '����������� ������', 'Liberian Dollar', 'LRD', '85.5'"+
		" UNION SELECT '��������� �����', 'Libyan Dinar', 'LYD', '1.245'"+
		" UNION SELECT '���������', 'Lilangeni', 'SZL', '10.5485'"+
		" UNION SELECT '���', 'Lithuanian Litas', 'LTL', '2.521'"+
		" UNION SELECT '����', 'Loti', 'LSL', '10.565'"+
		" UNION SELECT '������������� ������', 'Malagasy Ariary', 'MGA', '2354.0'"+
		" UNION SELECT '������������ �������', 'Malaysian Ringgit', 'MYR', '3.2795'"+
		" UNION SELECT '������������ �����', 'Mauritius Rupee', 'MUR', '30.31'"+
		" UNION SELECT '������������ ����', 'Mexican Peso', 'MXN', '13.0085'"+
		" UNION SELECT '���������� ���', 'Moldavan Leu', 'MDL', '13.19'"+
		" UNION SELECT '������������ ������', 'Moroccan Dirham', 'MAD', '8.1941'"+
		" UNION SELECT '������������ �������', 'Mozambique Metical', 'MXN', '13.0085'"+
		" UNION SELECT '�����', 'Naira', 'NGN', '164.0'"+
		" UNION SELECT '�����', 'Nakfa', 'ERN', '15.1'"+
		" UNION SELECT '������ �������', 'Namibia Dollar', 'NAD', '10.55'"+
		" UNION SELECT '���������� �����', 'Nepalese Rupee', 'NPR', '96.91'"+
		" UNION SELECT '������������� ���������� �������', 'Netherlands Antiilean Guilder', 'ANG', '1.79'"+
		" UNION SELECT '����� ����������� �����', 'New Israeli Sheqel', 'ILS', '3.4839'"+
		" UNION SELECT '����� ����������� ������', 'New Taiwan Dollar', 'TWD', '30.224'"+
		" UNION SELECT '�������������� ������', 'New Zealand Dollar', 'NZD', '1.1633'"+
		" UNION SELECT '��������', 'Ngultrum', 'BTN', '60.185'"+
		" UNION SELECT '��������������� ����', 'North Korean Won', 'KPW', '900.0'"+
		" UNION SELECT '���������� �����', 'Norwegian Krone', 'NOK', '6.001'"+
		" UNION SELECT '����� ����', 'Nuevo Sol', 'PEN', '2.807'"+
		" UNION SELECT '����', 'Ouguiya', 'MRO', '290.95'"+
		" UNION SELECT '������������ �����', 'Pakistan Rupee', 'PKR', '98.12'"+
		" UNION SELECT '������', 'Pataca', 'MOP', '7.9896'"+
		" UNION SELECT '������', 'Paanga', 'TOP', '1.8381'"+
		" UNION SELECT '����������� ����', 'Peso Uruguayo', 'UYU', '22.92'"+
		" UNION SELECT '������������ ����', 'Philippine Peso', 'PHP', '44.955'"+
		" UNION SELECT '���� ����������', 'Pound Sterling', 'GBP', '0.6033'"+
		" UNION SELECT '����', 'Pula', 'BWP', '8.8261'"+
		" UNION SELECT '��������� ����', 'Qatari Rial', 'QAR', '3.6413'"+
		" UNION SELECT '�������', 'Quetzal', 'GTQ', '7.7735'"+
		" UNION SELECT '����', 'Rand', 'ZAR', '10.5603'"+
		" UNION SELECT '�������� ����', 'Rial Omani', 'OMR', '0.3848'"+
		" UNION SELECT '�����', 'Riel', 'KHR', '4004.0'"+
		" UNION SELECT '����� ��������� ���', 'Romanian Leu', 'RON', '3.2543'"+
		" UNION SELECT '�����', 'Rupiah', 'IDR', '11323.0'"+
		" UNION SELECT '���������� �����', 'Russian Ruble', 'RUB', '35.3139'"+
		" UNION SELECT '����� ������', 'RWANDA Franc', 'RWF', '681.0'"+
		" UNION SELECT '���� ������ �����', 'Saint Helena Pound', 'SHP', '0.6032'"+
		" UNION SELECT '���������� ����', 'Saudi Riyal', 'SAR', '3.7504'"+
		" UNION SELECT '�������� �����', 'Serbian Dinar', 'RSD', '84.2705'"+
		" UNION SELECT '������������ ������', 'Singapore Dollar', 'SGD', '1.2588'"+
		" UNION SELECT '����������� �����', 'Seychelles Rupee', 'SCR', '11.9985'"+
		" UNION SELECT '������ ����������� ��������', 'Solomon Islands Dollar', 'SBD', '7.2747'"+
		" UNION SELECT '���', 'Som', 'KGS', '54.489'"+
		" UNION SELECT '����������� �������', 'Somali Shiling', 'SOS', '1001.5'"+
		" UNION SELECT '������', 'Somoni', 'TJS', '4.8502'"+
		" UNION SELECT '���-���������� �����', 'Sri Lanka Rupee', 'LKR', '130.6'"+
		" UNION SELECT '��������� ����', 'Sudanese Pound', 'SDG', '5.6863'"+
		" UNION SELECT '����������� ������', 'Surinam Dollar', 'SRD', '3.2725'"+
		" UNION SELECT '�������� �����', 'Swedish Krona', 'SEK', '6.5574'"+
		" UNION SELECT '����������� �����', 'Swiss Franc', 'CHF', '0.8921'"+
		" UNION SELECT '��������� ����', 'Syrian Pound', 'SYP', '144.45'"+
		" UNION SELECT '����', 'Taka', 'BDT', '77.6'"+
		" UNION SELECT '����', 'Tala', 'WST', '2.3071'"+
		" UNION SELECT '������������ �������', 'Tanzanian Shiling', 'TZS', '1630.5'"+
		" UNION SELECT '�����', 'Tenge', 'KZT', '182.09'"+
		" UNION SELECT '������ ��������� � ������', 'Trinidad and Tobago Dollar', 'TTD', '6.4405'"+
		" UNION SELECT '������', 'Tugrik', 'MNT', '1777.5'"+
		" UNION SELECT '��������� �����', 'Tunisian Dinar', 'TND', '1.5908'"+
		" UNION SELECT '�������� ����', 'Turkish Lira', 'TRY', '2.1135'"+
		" UNION SELECT '����� ����������� �����', 'Turkmenistan New Manat', 'TMT', '2.85'"+
		" UNION SELECT '������ ���', 'UAE Dirham', 'AED', '3.6731'"+
		" UNION SELECT '����������� �������', 'Uganda Shiling', 'UGX', '2563.0'"+
		" UNION SELECT '������ ���', 'US Dollar', 'USD', '1.0'"+
		" UNION SELECT '��������� ���', 'Uzbekistan Sum', 'UZS', '2262.9299'"+
		" UNION SELECT '����', 'Vatu', 'VUV', '94.52'"+
		" UNION SELECT '����', 'Won', 'KRW', '1052.96'"+
		" UNION SELECT '��������� ����', 'Yemeni Rial', 'YER', '214.925'"+
		" UNION SELECT '����', 'Yen', 'JPY', '103.285'"+
		" UNION SELECT '����', 'Yuan Renminbi', 'CNY', '6.2112'"+
		" UNION SELECT '���������� �����', 'Zambian Kwacha', 'ZMW', '6.15'"+
		" UNION SELECT '������ ��������', 'Zimbabwe Dollar', 'ZWL', '322.355'"+
		" UNION SELECT '������', 'Zloty', 'PLN', '3.0387'";
	
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
