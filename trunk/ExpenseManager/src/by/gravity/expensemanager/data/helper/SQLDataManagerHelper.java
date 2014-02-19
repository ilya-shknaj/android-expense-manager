package by.gravity.expensemanager.data.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import by.gravity.expensemanager.model.CurrencyModel;

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
			+ " DATE,"
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
			+ " VARCHAR (50))";
	
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
			+ SQLConstants.FIELD_SYMBOL
			+ " VARCHAR (10));";

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
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EXPENCE);
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_CURRENCY);
		db.execSQL(CREATE_TABLE_EXPENCE_CATEGORY);
		db.execSQL(CREATE_TABLE_PAYMENT_METHODS);
		addCurrency(db);
	}
	
	private void addCurrency(SQLiteDatabase database) {
		List<CurrencyModel> currencyList = new ArrayList<CurrencyModel>();
		currencyList.add(new CurrencyModel("BYR", "Беларусский рубль", "BYR"));
		currencyList.add(new CurrencyModel("USD", "Доллар", "$"));
		currencyList.add(new CurrencyModel("EUR", "Евро", "€"));

		ContentValues values = null;
		CurrencyModel currencyModel = null;
		for (int i = 0; i < currencyList.size(); i++) {
			currencyModel = currencyList.get(i);
			values = new ContentValues();
			values.put(SQLConstants.FIELD_CODE, currencyModel.getCode());
			values.put(SQLConstants.FIELD_NAME, currencyModel.getName());
			values.put(SQLConstants.FIELD_SYMBOL, currencyModel.getSymbol());
			database.insert(SQLConstants.TABLE_CURRENCY, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
