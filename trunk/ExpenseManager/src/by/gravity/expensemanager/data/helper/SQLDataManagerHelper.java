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
		" SELECT '������' AS name, 'Afghani' AS name_en, 'AFN' as code"+
		" UNION SELECT '��������� �����', 'Algerian Dinar', 'DZD'"+
		" UNION SELECT '��������� ����', 'Armenian Dram', 'AMD'"+
		" UNION SELECT '���������� ������', 'Aruban Guilder', 'AWG'"+
		" UNION SELECT '������������� ������', 'Australian Dollar', 'AUD'"+
		" UNION SELECT '��������������� �����', 'Azerbaijanian Manat', 'AZN'"+
		" UNION SELECT '��������� ������', 'Bahamian Dollar', 'BSD'"+
		" UNION SELECT '����������� �����', 'Bahraini Dinar', 'BHD'"+
		" UNION SELECT '���', 'Baht', 'THB'"+
		" UNION SELECT '�������', 'Balboa', 'PAB'"+
		" UNION SELECT '������������ ������', 'Barbados Dollar', 'BBD'"+
		" UNION SELECT '����������� �����', 'Belarussian Ruble', 'BYR'"+
		" UNION SELECT '��������� ������', 'Belize Dollar', 'BZD'"+
		" UNION SELECT '���������� ������', 'Bermudian Dollar', 'BMD'"+
		" UNION SELECT '������� ������', 'Bolivar', 'VEF'"+
		" UNION SELECT '���������', 'Boliviano', 'BOB'"+
		" UNION SELECT '����������� ����', 'Brazilian Real', 'BRL'"+
		" UNION SELECT '���������� ������', 'Brunei Dollar', 'BGN'"+
		" UNION SELECT '������������ �����', 'Burundi Franc', 'BIF'"+
		" UNION SELECT '��������� ������', 'Canadian Dollar', 'CAD'"+
		" UNION SELECT '������ ����-�����', 'Cape Verder Escudo', 'CVE'"+
		" UNION SELECT '������ �������� ������ ', 'Cayman Islands Dollar', 'KYD'"+
		" UNION SELECT '����� ��� �����', 'CFA Franc BCEAO', 'XOF'"+
		" UNION SELECT '����� ��� ����', 'CFA Franc BEAF', 'XAF'"+
		" UNION SELECT '����� ���', 'CF� Franc', 'XPF'"+
		" UNION SELECT '��������� ����', 'Chilean Peso', 'CLP'"+
		" UNION SELECT '������������ ����', 'Columbian Peso', 'COP'"+
		" UNION SELECT '����� �����', 'Comoro Franc', 'KMF'"+
		" UNION SELECT '������������ �����', 'Congolese Franc', 'CDF'"+
		" UNION SELECT '�������������� �����', 'Convertible Mark', 'BAM'"+
		" UNION SELECT '������� �������', 'Corboda Oro', 'NIO'"+
		" UNION SELECT '�������������� �����', 'Costa Rican Colon', 'CRC'"+
		" UNION SELECT '���������� ����', 'Croatian Kuna', 'HRK'"+
		" UNION SELECT '��������� ����', 'Cuban Peso', 'CUP'"+
		" UNION SELECT '������� �����', 'Czech Koruna', 'CZK'"+
		" UNION SELECT '������', 'Dalasi', 'GMD'"+
		" UNION SELECT '������� �����', 'Danish Krone', 'DKK'"+
		" UNION SELECT '�����', 'Denar', 'MKD'"+
		" UNION SELECT '����� �������', 'Djibouti Franc', 'DJF'"+
		" UNION SELECT '�����', 'Dobra', 'STD'"+
		" UNION SELECT '������������� ����', 'Dominican Peso', 'DOP'"+
		" UNION SELECT '����', 'Dong', 'VND'"+
		" UNION SELECT '��������-��������� ������', 'East Caribbean Dolar', 'XCD'"+
		" UNION SELECT '���������� ����', 'Egyptian Pound', 'EGP'"+
		" UNION SELECT '������������� �����', 'EL Salvador Colon', 'SVC'"+
		" UNION SELECT '��������� ���', 'Ethiopian Birr', 'ETB'"+
		" UNION SELECT '����', 'Euro', 'EUR'"+
		" UNION SELECT '���� ������������ ��������', 'Falkland Islands Pound', 'FKP'"+
		" UNION SELECT '������ �����', 'Fiji Dollar', 'FJD'"+
		" UNION SELECT '������', 'Forint', 'HUF'"+
		" UNION SELECT '������� ����', 'Ghana Cedi', 'GHS'"+
		" UNION SELECT '������������� ����', 'Gibraltar Pound', 'GIP'"+
		" UNION SELECT '����', 'Gourde', 'HTG'"+
		" UNION SELECT '�������', 'Guarani', 'PYG'"+
		" UNION SELECT '���������� �����', 'Guinea Franc', 'GNF'"+
		" UNION SELECT '��������� ������', 'Guyana Dollar', 'GYD'"+
		" UNION SELECT '����������� ������', 'Hong Kong Dollar', 'HKD'"+
		" UNION SELECT '������', 'Hryvnia', 'UAH'"+
		" UNION SELECT '���������� �����', 'Iceland Krona', 'ISK'"+
		" UNION SELECT '��������� �����', 'Indian Rupee', 'INR'"+
		" UNION SELECT '�������� ����', 'Iranian Rial', 'IRR'"+
		" UNION SELECT '�������� �����', 'Iraqi Dinar', 'IQD'"+
		" UNION SELECT '�������� ������', 'Jamaican Dollar', 'JMD'"+
		" UNION SELECT '���������� �����', 'Jordanian Dinar', 'JOD'"+
		" UNION SELECT '��������� �������', 'Kenyan Shilling', 'KES'"+
		" UNION SELECT '����', 'Kina', 'PGK'"+
		" UNION SELECT '���', 'Kip', 'LAK'"+
		" UNION SELECT '���������� �����', 'Kiwaiti Dinar', 'KWD'"+
		" UNION SELECT '�����', 'Kwacha', 'MWK'"+
		" UNION SELECT '������', 'Kwanza', 'AOA'"+
		" UNION SELECT '����', 'Kyat', 'MMK'"+
		" UNION SELECT '����', 'Lari', 'GEL'"+
		" UNION SELECT '��������� ����', 'Lebanese Pound', 'LBP'"+
		" UNION SELECT '���', 'Lek', 'ALL'"+
		" UNION SELECT '�������', 'Lempira', 'HNL'"+
		" UNION SELECT '�����', 'Leone', 'SLL'"+
		" UNION SELECT '����������� ������', 'Liberian Dollar', 'LRD'"+
		" UNION SELECT '��������� �����', 'Libyan Dinar', 'LYD'"+
		" UNION SELECT '���������', 'Lilangeni', 'SZL'"+
		" UNION SELECT '���', 'Lithuanian Litas', 'LTL'"+
		" UNION SELECT '����', 'Loti', 'LSL'"+
		" UNION SELECT '������������� ������', 'Malagasy Ariary', 'MGA'"+
		" UNION SELECT '������������ �������', 'Malaysian Ringgit', 'MYR'"+
		" UNION SELECT '������������ �����', 'Mauritius Rupee', 'MUR'"+
		" UNION SELECT '������������ ����', 'Mexican Peso', 'MXN'"+
		" UNION SELECT '���������� ���', 'Moldavan Leu', 'MDL'"+
		" UNION SELECT '������������ ������', 'Moroccan Dirham', 'MAD'"+
		" UNION SELECT '������������ �������', 'Mozambique Metical', 'MXN'"+
		" UNION SELECT '�����', 'Naira', 'NGN'"+
		" UNION SELECT '�����', 'Nakfa', 'ERN'"+
		" UNION SELECT '������ �������', 'Namibia Dollar', 'NAD'"+
		" UNION SELECT '���������� �����', 'Nepalese Rupee', 'NPR'"+
		" UNION SELECT '������������� ���������� �������', 'Netherlands Antiilean Guilder', '���'"+
		" UNION SELECT '����� ����������� �����', 'New Israeli Sheqel', 'ISL'"+
		" UNION SELECT '����� ����������� ������', 'New Taiwan Dollar', 'TWD'"+
		" UNION SELECT '�������������� ������', 'New Zealand Dollar', 'NZD'"+
		" UNION SELECT '��������', 'Ngultrum', 'BTN'"+
		" UNION SELECT '��������������� ����', 'North Korean Won', 'KPW'"+
		" UNION SELECT '���������� �����', 'Norwegian Krone', 'NOK'"+
		" UNION SELECT '����� ����', 'Nuevo Sol', 'PEN'"+
		" UNION SELECT '����', 'Ouguiya', 'MRO'"+
		" UNION SELECT '������������ �����', 'Pakistan Rupee', 'PKR'"+
		" UNION SELECT '������', 'Pataca', 'MOP'"+
		" UNION SELECT '������', 'Paanga', 'TOP'"+
		" UNION SELECT '����������� ����', 'Peso Uruguayo', 'UYU'"+
		" UNION SELECT '������������ ����', 'Philippine Peso', 'PHP'"+
		" UNION SELECT '���� ����������', 'Pound Sterling', 'GBP'"+
		" UNION SELECT '����', 'Pula', 'BWP'"+
		" UNION SELECT '��������� ����', 'Qatari Rial', 'QAR'"+
		" UNION SELECT '�������', 'Quetzal', 'GTQ'"+
		" UNION SELECT '����', 'Rand', 'ZAR'"+
		" UNION SELECT '�������� ����', 'Rial Omani', 'OMR'"+
		" UNION SELECT '�����', 'Riel', 'KHR'"+
		" UNION SELECT '����� ��������� ���', 'Romanian Leu', 'RON'"+
		" UNION SELECT '�����', 'Rupiah', 'IDR'"+
		" UNION SELECT '���������� �����', 'Russian Ruble', 'RUB'"+
		" UNION SELECT '����� ������', 'RWANDA Franc', 'RWF'"+
		" UNION SELECT '���� ������ �����', 'Saint Helena Pound', 'SHP'"+
		" UNION SELECT '���������� ����', 'Saudi Riyal', 'SAR'"+
		" UNION SELECT '�������� �����', 'Serbian Dinar', 'RSD'"+
		" UNION SELECT '����������� �����', 'Seychelles Rupee', 'SRC'"+
		" UNION SELECT '������������ ������', 'Singapore Dollar', 'SGD'"+
		" UNION SELECT '����������� �����', 'Seychelles Rupee', 'SCR'"+
		" UNION SELECT '������ ����������� ��������', 'Solomon Islands Dollar', 'SBD'"+
		" UNION SELECT '���', 'Som', 'KGS'"+
		" UNION SELECT '����������� �������', 'Somali Shiling', 'SOS'"+
		" UNION SELECT '������', 'Somoni', 'TJS'"+
		" UNION SELECT '������������� ����', 'South Sudanese Pound', 'SSP'"+
		" UNION SELECT '���-���������� �����', 'Sri Lanka Rupee', 'LKR'"+
		" UNION SELECT '��������� ����', 'Sudanese Pound', 'SDG'"+
		" UNION SELECT '����������� ������', 'Surinam Dollar', 'SRD'"+
		" UNION SELECT '�������� �����', 'Swedish Krona', 'SEK'"+
		" UNION SELECT '����������� �����', 'Swiss Franc', 'CHF'"+
		" UNION SELECT '��������� ����', 'Syrian Pound', 'SYP'"+
		" UNION SELECT '����', 'Taka', 'BDT'"+
		" UNION SELECT '����', 'Tala', 'WST'"+
		" UNION SELECT '������������ �������', 'Tanzanian Shiling', 'TZS'"+
		" UNION SELECT '�����', 'Tenge', 'KZT'"+
		" UNION SELECT '������ ��������� � ������', 'Trinidad and Tobago Dollar', 'TTD'"+
		" UNION SELECT '������', 'Tugrik', 'MNT'"+
		" UNION SELECT '��������� �����', 'Tunisian Dinar', 'TND'"+
		" UNION SELECT '�������� ����', 'Turkish Lira', 'TRY'"+
		" UNION SELECT '����� ����������� �����', 'Turkmenistan New Manat', 'TMT'"+
		" UNION SELECT '������ ���', 'UAE Dirham', 'AED'"+
		" UNION SELECT '����������� �������', 'Uganda Shiling', 'UGX'"+
		" UNION SELECT '������ ���', 'US Dollar', 'USD'"+
		" UNION SELECT '��������� ���', 'Uzbekistan Sum', 'UZS'"+
		" UNION SELECT '����', 'Vatu', 'VUV'"+
		" UNION SELECT '����', 'Won', 'KRW'"+
		" UNION SELECT '��������� ����', 'Yemeni Rial', 'YER'"+
		" UNION SELECT '����', 'Yen', 'JPY'"+
		" UNION SELECT '����', 'Yuan Renminbi', 'CNY'"+
		" UNION SELECT '���������� �����', 'Zambian Kwacha', 'ZMW'"+
		" UNION SELECT '������ ��������', 'Zimbabwe Dollar', 'ZWL'"+
		" UNION SELECT '������', 'Zloty', 'PLN'";
	
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
