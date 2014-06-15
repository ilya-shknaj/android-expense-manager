package by.gravity.expensemanager.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import by.gravity.common.task.AsyncTask;
import by.gravity.common.task.OnLoadCompleteListener;
import by.gravity.common.utils.ContextHolder;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.data.helper.SQLDataManagerHelper;
import by.gravity.expensemanager.model.CategoryModel;
import by.gravity.expensemanager.model.ExpenseModel;
import by.gravity.expensemanager.model.PaymentMethodModel;
import by.gravity.expensemanager.model.PeriodDate;
import by.gravity.expensemanager.model.RateModel;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.GlobalUtils;

public class SQLDataManager {

	private SQLDataManagerHelper sqlHelper;
	private SQLiteDatabase database;

	private static SQLDataManager instance;

	private SQLDataManager() {

		sqlHelper = new SQLDataManagerHelper(ContextHolder.getContext());
		database = sqlHelper.getWritableDatabase();
	}

	public static SQLDataManager getInstance() {

		if (instance == null) {
			instance = new SQLDataManager();
		}
		return instance;
	}

	public void addExpense(final String amount, final String currency, final Long date, final Long time, final List<String> categories,
			final String note, final String paymentMethod, OnLoadCompleteListener<Boolean> onLoadCompleteListener) {

		new AsyncTask<Void, Void, Boolean>(onLoadCompleteListener) {

			@Override
			protected Boolean doInBackground(Void... params) {

				updateCategories(categories);

				ContentValues values = new ContentValues();
				Long currencyId = getCurrencyId(currency);
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, currencyId);
				values.put(SQLConstants.FIELD_DATE, date);
				values.put(SQLConstants.FIELD_TIME, time);
				if (!StringUtil.isEmpty(note)) {
					values.put(SQLConstants.FIELD_NOTE, note);
				}
				PaymentMethodModel paymentMethodModel = getPaymentMethod(paymentMethod);
				if (paymentMethodModel != null) {
					values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodModel.getId());
				}
				long expenseId = database.insert(SQLConstants.TABLE_EXPENSE, null, values);
				addExpenseCategories(categories, expenseId);
				updateUsageCategoryCount(categories);
				double rate = getRate(currencyId, paymentMethodModel.getCurrencyId());

				substractFromPaymentHistory(expenseId, rate);
				return null;

			}
		}.start();
	}

	public void updateExpense(final long id, final String amount, final String currency, final Long date, final Long time,
			final List<String> categories, final String note, final String paymentMethod,
			final OnLoadCompleteListener<Void> onCompletionListener) {

		new AsyncTask<Void, Void, Void>(onCompletionListener) {

			@Override
			protected Void doInBackground(Void... params) {

				updateCategories(categories);

				ContentValues values = new ContentValues();
				Long currencyId = getCurrencyId(currency);
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, currencyId);
				values.put(SQLConstants.FIELD_DATE, date);
				values.put(SQLConstants.FIELD_TIME, time);
				values.put(SQLConstants.FIELD_NOTE, note);

				PaymentMethodModel paymentMethodModel = getPaymentMethod(paymentMethod);
				values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodModel.getId());

				double rate = getRate(currencyId, paymentMethodModel.getCurrencyId());

				ExpenseModel expenseModel = getExpenseById(id);

				Long prevCurrencyId = expenseModel.getCurrencyId();
				double prevRate;
				if (prevCurrencyId != currencyId) {
					prevRate = getRate(prevCurrencyId, paymentMethodModel.getCurrencyId());
				} else {
					prevRate = rate;
				}

				addToPaymentHistory(id, prevRate);

				paymentMethodModel = getPaymentMethod(paymentMethod);

				database.update(SQLConstants.TABLE_EXPENSE, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
				expenseModel = getExpenseById(id);
				deleteExpenseCategories(id);
				addExpenseCategories(categories, id);

				substractFromPaymentHistory(id, rate);
				paymentMethodModel = getPaymentMethod(paymentMethod);
				expenseModel = getExpenseById(id);

				return null;
			}
		}.start();

	}

	public void addPaymentMethod(final String name, final String note, final String balance, final String currency,
			final OnLoadCompleteListener<Void> onCompleteListener) {

		new AsyncTask<Void, Void, Void>(onCompleteListener) {

			@Override
			protected Void doInBackground(Void... arg0) {

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_NAME, StringUtil.uppercaseFirstLetter(name));
				values.put(SQLConstants.FIELD_NOTE, note);
				values.put(SQLConstants.FIELD_BALANCE,
						!StringUtil.isEmpty(balance) ? balance.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING) : "0");

				Long currencyID = getCurrencyId(currency);
				values.put(SQLConstants.FIELD_CURRENCY, currencyID);
				database.insert(SQLConstants.TABLE_PAYMENT_METHODS, null, values);
				return null;
			}
		}.start();

	}

	public void updatePaymentMethod(final Long id, final String name, final String note, final String balance, final String currency,
			final OnLoadCompleteListener<Void> onCompleteListener) {

		new AsyncTask<Void, Void, Void>(onCompleteListener) {

			@Override
			protected Void doInBackground(Void... params) {

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_NAME, StringUtil.uppercaseFirstLetter(name));
				values.put(SQLConstants.FIELD_NOTE, note);
				values.put(SQLConstants.FIELD_BALANCE, balance.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));

				Long currencyID = getCurrencyId(currency);
				values.put(SQLConstants.FIELD_CURRENCY, currencyID);

				database.update(SQLConstants.TABLE_PAYMENT_METHODS, values, SQLConstants.FIELD_ID + "=?",
						new String[] { String.valueOf(id) });
				return null;
			}
		}.start();
	}

	public Cursor getCurrenciesShortCursor() {

		return database.query(SQLConstants.TABLE_CURRENCY, null, SQLConstants.FIELD_IS_USED + "=?", new String[] { "1" }, null, null,
				SQLConstants.FIELD_CODE);
	}

	public Cursor getCurrenciesFullCursor() {

		String currencyTableName = ContextHolder.getContext().getString(R.string.usedCurrencyName).equals("RU") ? SQLConstants.FIELD_NAME
				: SQLConstants.FIELD_NAME_EN;
		return database.query(SQLConstants.TABLE_CURRENCY, new String[] { SQLConstants.FIELD_ID, SQLConstants.FIELD_CODE,
				currencyTableName + " AS " + SQLConstants.FIELD_NAME, SQLConstants.FIELD_IS_USED, SQLConstants.FIELD_RATE }, null, null,
				null, null, SQLConstants.FIELD_IS_USED + " DESC , " + SQLConstants.FIELD_NAME);
	}

	public Cursor getCategoriesCursor() {

		return database.query(SQLConstants.TABLE_CATEGORY, null, null, null, null, null, SQLConstants.FIELD_USAGE_COUNT + " DESC");
	}

	public List<String> parseCategoriesListCursor(Cursor cursor) {

		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			categories.add(parseCategoriesCursor(cursor));
		}

		cursor.close();

		return categories;

	}

	public String parseCategoriesCursor(Cursor cursor) {

		return cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
	}

	private static final String EXPENSE_GROUPED_BY_DATE_AND_AMOUNT_QUERY = "SELECT " + SQLConstants.FIELD_ID + ","
			+ SQLConstants.FIELD_DATE + ", group_concat(" + SQLConstants.FIELD_SUM_AMOUNT + ", '') AS " + SQLConstants.FIELD_SUM_AMOUNT
			+ " FROM(SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + "," + SQLConstants.FIELD_DATE + ", sum("
			+ SQLConstants.FIELD_AMOUNT + ") || ' '|| " + SQLConstants.FIELD_CODE + " || '" + Constants.NEW_STRING + "' "
			+ SQLConstants.FIELD_SUM_AMOUNT + " FROM " + SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + " WHERE "
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " GROUP BY "
			+ SQLConstants.FIELD_DATE + "," + SQLConstants.FIELD_CURRENCY + ")WHERE " + SQLConstants.FIELD_DATE + " BETWEEN "
			+ "? AND ? GROUP BY " + SQLConstants.FIELD_DATE;

	public Cursor getGroupedByDateCursor() {

		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(EXPENSE_GROUPED_BY_DATE_AND_AMOUNT_QUERY,
				new String[] { periodDate.getStartDate(), periodDate.getEndDate() });
	}

	private static final String EXPENSE_GROUPED_BY_CATEGORY_NAME_QUERY = "SELECT " + SQLConstants.FIELD_ID + "," + SQLConstants.FIELD_NAME
			+ "," + "group_concat(" + SQLConstants.FIELD_SUM_AMOUNT + ",'') AS " + SQLConstants.FIELD_SUM_AMOUNT + " FROM( SELECT  "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + " AS " + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + "," + "sum(" + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_AMOUNT + ") || ' '||  " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + "|| '"
			+ Constants.NEW_STRING + "' AS " + SQLConstants.FIELD_SUM_AMOUNT + " FROM " + SQLConstants.TABLE_EXPENSE + ","
			+ SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + " WHERE "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "="
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_EXPENSE_ID + "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_DATE + " BETWEEN ? AND ? GROUP BY " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_CURRENCY + "," + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + ")GROUP BY "
			+ SQLConstants.FIELD_NAME;

	public Cursor getGroupedByCategoryNameCursor() {

		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(EXPENSE_GROUPED_BY_CATEGORY_NAME_QUERY,
				new String[] { periodDate.getStartDate(), periodDate.getEndDate() });
	}

	private static final String GET_CHILD_GROUPED_BY_DATE_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID
			+ "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_TIME + "," + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_AMOUNT + " || ' ' || " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " AS "
			+ SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + " AS " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM "
			+ SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_PAYMENT_METHODS + " WHERE "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + "="
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_PAYMENT_METHOD + " AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_DATE + "=?";

	public Cursor getChildGroupedByDateExpenseCursor(Long date) {

		return database.rawQuery(GET_CHILD_GROUPED_BY_DATE_QUERY, new String[] { String.valueOf(date) });
	}

	private static final String GET_EXPENSE_BY_ID_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.FIELD_TIME + "," + SQLConstants.FIELD_DATE + "," + SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_CURRENCY
			+ "." + SQLConstants.FIELD_CODE + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + " AS " + SQLConstants.FIELD_PAYMENT_METHOD
			+ ", group_concat(" + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + ",',') AS " + SQLConstants.FIELD_NAME
			+ " FROM " + SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + ","
			+ SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_PAYMENT_METHODS + " WHERE " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + "=?" + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_EXPENSE_ID + "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "=" + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_PAYMENT_METHOD + "="
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID;

	public Cursor getExpenseByIdCursor(Long id) {

		return database.rawQuery(GET_EXPENSE_BY_ID_QUERY, new String[] { String.valueOf(id) });
	}

	private static final String GET_CATEGORY_EXPENSE_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_DATE + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_TIME
			+ "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_AMOUNT + " || ' ' || " + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_CODE + " AS " + SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_NOTE + "," + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + " AS "
			+ SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + ","
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "," + SQLConstants.TABLE_PAYMENT_METHODS + " WHERE "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + "=" + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_PAYMENT_METHOD + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_EXPENSE_ID + "="
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_CATEGORY_ID + "= ? AND " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_DATE + " BETWEEN ? AND ?";

	public Cursor getGroupedByCategoryNameChildCursor(long category) {

		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(GET_CATEGORY_EXPENSE_QUERY,
				new String[] { String.valueOf(category), periodDate.getStartDate(), periodDate.getEndDate() });
	}

	public Cursor getGrouperdByCategoryNameCategoriesCursor(long expenseId) {

		return database.query(SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY,
				new String[] { SQLConstants.FIELD_NAME }, SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "="
						+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
						+ SQLConstants.FIELD_EXPENSE_ID + "=?", new String[] { String.valueOf(expenseId) }, null, null, null);
	}

	private static final String GET_PAYMENT_METHODS_QUERY = "SELECT " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID
			+ "," + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + "," + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_BALANCE + "," + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " FROM "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID;

	public Cursor getPaymentMethods() {

		return database.rawQuery(GET_PAYMENT_METHODS_QUERY, null);
	}

	private static final String GET_PAYMENT_METHODS_BY_ID_QUERY = "SELECT " + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_ID + "," + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_BALANCE + "," + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_CODE + "," + SQLConstants.FIELD_NOTE + " FROM " + SQLConstants.TABLE_PAYMENT_METHODS + ","
			+ SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_ID + "=?";

	public Cursor getPaymentMethod(Long id) {

		return database.rawQuery(GET_PAYMENT_METHODS_BY_ID_QUERY, new String[] { String.valueOf(id) });
	}

	private Long getPaymentMethodIdCurrency(Long paymentMethodId) {

		Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_CURRENCY },
				SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(paymentMethodId) }, null, null, null);
		cursor.moveToFirst();
		Long currrencyId = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_CURRENCY));
		cursor.close();

		return currrencyId;
	}

	private static final String GET_SUM_BALANCE = "SELECT group_concat(" + SQLConstants.FIELD_BALANCE + ",'" + Constants.NEW_STRING
			+ "') AS " + SQLConstants.FIELD_BALANCE + " FROM ( SELECT sum(" + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_BALANCE + ") || ' ' || " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " AS "
			+ SQLConstants.FIELD_BALANCE + " FROM " + SQLConstants.TABLE_PAYMENT_METHODS + "," + SQLConstants.TABLE_CURRENCY + " WHERE "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_ID + " GROUP BY " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_CURRENCY + ")";

	public Cursor getSumBalance() {

		return database.rawQuery(GET_SUM_BALANCE, null);
	}

	private Long getCategoryId(final String name) {

		Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_NAME + "=?",
				new String[] { name }, null, null, null);
		Long id = null;
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));
			}

			cursor.close();
		}

		return id;

	}

	private List<Long> getCategoriesId(final List<String> categories) {

		Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_NAME
				+ " IN(" + makePlaceholders(categories.size()) + ")", categories.toArray(new String[] {}), null, null, null);
		List<Long> categoriesId = new ArrayList<Long>();
		if (cursor != null && cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				categoriesId.add(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID)));
			}
			cursor.close();
		}

		return categoriesId;
	}

	private String makePlaceholders(int len) {

		if (len < 1) {
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			sb.append("?");
			for (int i = 1; i < len; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}

	public void addCategory(final String name) {

		addCategory(name, GlobalUtils.generateRandomColor());
	}

	public void addCategory(final String name, final Integer color) {

		ContentValues values = new ContentValues();
		values.put(SQLConstants.FIELD_NAME, name);
		values.put(SQLConstants.FIELD_COLOR, color);
		database.insert(SQLConstants.TABLE_CATEGORY, null, values);
	}

	private void updateCategories(final List<String> categories) {

		for (int i = 0; i < categories.size(); i++) {
			if (getCategoryId(categories.get(i)) == null) {
				categories.set(i, StringUtil.uppercaseFirstLetter(categories.get(i)));
				addCategory(categories.get(i));
			}
		}
	}

	private void addExpenseCategories(final List<String> categories, final long expenseId) {

		List<Long> categoriesId = getCategoriesId(categories);
		ContentValues values = null;
		for (int i = 0; i < categoriesId.size(); i++) {
			values = new ContentValues();
			values.put(SQLConstants.FIELD_CATEGORY_ID, categoriesId.get(i));
			values.put(SQLConstants.FIELD_EXPENSE_ID, expenseId);
			database.insert(SQLConstants.TABLE_EXPENSE_CATEGORY, null, values);
		}
	}

	private void deleteExpenseCategories(final long expenseId) {

		database.delete(SQLConstants.TABLE_EXPENSE_CATEGORY, SQLConstants.FIELD_EXPENSE_ID + "=?",
				new String[] { String.valueOf(expenseId) });
	}

	private Long getCurrencyId(final String currency) {

		Cursor cursor = database.query(SQLConstants.TABLE_CURRENCY, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_CODE + "=?",
				new String[] { currency }, null, null, null);
		Long id = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));

			cursor.close();
		}

		return id;
	}

	private PaymentMethodModel getPaymentMethod(final String accountName) {

		if (StringUtil.isEmpty(accountName)) {
			return null;
		}
		Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_ID,
				SQLConstants.FIELD_CURRENCY, SQLConstants.FIELD_BALANCE }, SQLConstants.FIELD_NAME + "=?", new String[] { accountName },
				null, null, null);
		PaymentMethodModel paymentMethodModel = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			paymentMethodModel = new PaymentMethodModel();
			paymentMethodModel.setId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID)));
			paymentMethodModel.setCurrencyId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_CURRENCY)));
			paymentMethodModel.setBalance(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_BALANCE)));

			cursor.close();
		}

		return paymentMethodModel;
	}

	private void updateUsageCategoryCount(final List<String> categories) {

		Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, null,
				SQLConstants.FIELD_NAME + " IN(" + makePlaceholders(categories.size()) + ")", categories.toArray(new String[] {}), null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				long id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));
				int usageCount = cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_USAGE_COUNT));

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_USAGE_COUNT, ++usageCount);
				values.put(SQLConstants.FIELD_LAST_USAGE_TIME, System.currentTimeMillis());

				database.update(SQLConstants.TABLE_CATEGORY, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
			}
			cursor.close();
		}
	}

	public void deletePayment(Long id) {

		ExpenseModel expenseModel = getExpenseById(id);
		Long paymentMethodCurrencyId = getPaymentMethodIdCurrency(expenseModel.getPaymentMethodId());
		double rate = getRate(expenseModel.getCurrencyId(), paymentMethodCurrencyId);

		addToPaymentHistory(id, rate);
		database.delete(SQLConstants.TABLE_EXPENSE, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
	}

	private static final String SUBSTRACT_FROM_PAYMENT_METHOD_QUERY = "UPDATE " + SQLConstants.TABLE_PAYMENT_METHODS + " SET "
			+ SQLConstants.FIELD_BALANCE + "=" + " ROUND (" + SQLConstants.FIELD_BALANCE + " - (SELECT " + SQLConstants.FIELD_AMOUNT
			+ " * ? FROM " + SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID + "= ?),2) WHERE " + SQLConstants.FIELD_ID
			+ "= (SELECT " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID
			+ "= ?)";

	public void substractFromPaymentHistory(Long paymentId, double rate) {

		database.execSQL(SUBSTRACT_FROM_PAYMENT_METHOD_QUERY,
				new String[] { String.valueOf(rate), String.valueOf(paymentId), String.valueOf(paymentId) });
	}

	private static final String ADD_TO_PAYMENT_METHOD_QUERY = "UPDATE " + SQLConstants.TABLE_PAYMENT_METHODS + " SET "
			+ SQLConstants.FIELD_BALANCE + "=" + " ROUND (" + SQLConstants.FIELD_BALANCE + " + (SELECT " + SQLConstants.FIELD_AMOUNT
			+ " * ? FROM " + SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID + "= ?),2) WHERE " + SQLConstants.FIELD_ID
			+ "= (SELECT " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID
			+ "= ?)";

	public void addToPaymentHistory(Long expensetId, double rate) {

		database.execSQL(ADD_TO_PAYMENT_METHOD_QUERY,
				new String[] { String.valueOf(rate), String.valueOf(expensetId), String.valueOf(expensetId) });
	}

	public void deletePaymentMethod(long id) {

		database.delete(SQLConstants.TABLE_PAYMENT_METHODS, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
		database.delete(SQLConstants.TABLE_EXPENSE, SQLConstants.FIELD_PAYMENT_METHOD + "=?", new String[] { String.valueOf(id) });
	}

	public Cursor getCodes() {

		return database.query(SQLConstants.TABLE_CURRENCY, new String[] { SQLConstants.FIELD_ID, SQLConstants.FIELD_CODE }, null, null,
				null, null, null);
	}

	private static final String CLEAR_SHOWED_CURRENCY_QUERY = "UPDATE " + SQLConstants.TABLE_CURRENCY + " SET "
			+ SQLConstants.FIELD_IS_USED + "= 0";

	private static final String SET_USED_CURRENCY_QUERY = "UPDATE " + SQLConstants.TABLE_CURRENCY + " SET " + SQLConstants.FIELD_IS_USED
			+ " = 1 WHERE " + SQLConstants.FIELD_ID + " IN (%s)";

	public void updateUsedCurrencies(long[] ids) {

		database.beginTransaction();
		database.execSQL(CLEAR_SHOWED_CURRENCY_QUERY);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			builder.append(ids[i]);
			if (i + 1 < ids.length) {
				builder.append(",");
			}
		}
		database.execSQL(String.format(SET_USED_CURRENCY_QUERY, builder.toString()));
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	private static final String UPDATE_RATES_QUERY = "UPDATE " + SQLConstants.TABLE_CURRENCY + " SET " + SQLConstants.FIELD_RATE + "= %s"
			+ " WHERE " + SQLConstants.FIELD_CODE + "= '%s'";

	public void updateRate(RateModel rateModel) {

		database.execSQL(String.format(UPDATE_RATES_QUERY, rateModel.getRate(), rateModel.getCode()));
	}

	public void updateRates(List<RateModel> rateList) {

		database.beginTransaction();
		for (int i = 0; i < rateList.size(); i++) {
			database.execSQL(String.format(UPDATE_RATES_QUERY, rateList.get(i).getRate(), rateList.get(i).getCode()));
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public List<RateModel> getRates(String[] codes) {

		Cursor cursor = database.query(SQLConstants.TABLE_CURRENCY, new String[] { SQLConstants.FIELD_RATE, SQLConstants.FIELD_CODE },
				SQLConstants.FIELD_ID + " IN (" + makePlaceholders(codes.length) + ")", codes, null, null, null);

		List<RateModel> rateList = new ArrayList<RateModel>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			RateModel rateModel = new RateModel();
			rateModel.setCode(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
			rateModel.setRate(cursor.getDouble(cursor.getColumnIndex(SQLConstants.FIELD_RATE)));
			rateList.add(rateModel);

		}

		return rateList;

	}

	private ExpenseModel getExpenseById(long id) {

		ExpenseModel expenseModel = null;
		Cursor cursor = database.query(SQLConstants.TABLE_EXPENSE, null, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null);
		if (cursor.moveToFirst()) {
			expenseModel = new ExpenseModel();
			expenseModel.setCurrencyId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_CURRENCY)));
			expenseModel.setPaymentMethodId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_PAYMENT_METHOD)));
			expenseModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)));

			cursor.close();
		}

		return expenseModel;
	}

	private double getRate(Long currencyId, Long paymentMethodCurrencyId) {

		double rate;
		if (currencyId == paymentMethodCurrencyId) {
			rate = 1;
		} else {
			List<RateModel> rateList = getRates(new String[] { String.valueOf(currencyId), String.valueOf(paymentMethodCurrencyId) });
			rate = GlobalUtils.getRate(rateList.get(0).getCode(), rateList.get(0).getRate(), rateList.get(1).getCode(), rateList.get(1)
					.getRate());
		}

		return rate;
	}

	private static final String GET_CATEGORY_BY_ID_QUERY = "SELECT " + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + ","
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_COLOR + "," + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_USAGE_COUNT + "," + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_LAST_USAGE_TIME + ","
			+ SQLConstants.FIELD_AMOUNT + "," + SQLConstants.FIELD_CURRENCY + " FROM  " + SQLConstants.TABLE_CATEGORY
			+ " LEFT JOIN (SELECT sum(" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_AMOUNT + ")/COUNT(*)  as "
			+ SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " as "
			+ SQLConstants.FIELD_CURRENCY + " FROM " + SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + ","
			+ SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_EXPENSE_ID + "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "= ? AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " GROUP BY "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + ") WHERE " + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_ID + "= ?";

	public Cursor getCategoryByID(long id) {

		return database.rawQuery(GET_CATEGORY_BY_ID_QUERY, new String[] { String.valueOf(id), String.valueOf(id) });
	}

	public CategoryModel parseCategory(Cursor cursor) {

		CategoryModel categoryModel = new CategoryModel();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (i == 0) {
				categoryModel.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
				categoryModel.setColor(cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_COLOR)));
				categoryModel.setCountUsage(String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_USAGE_COUNT))));
				long lastUsageTime = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_LAST_USAGE_TIME));
				if (lastUsageTime != 0) {
					categoryModel.setLastUsageTime(new Date(lastUsageTime));
				}
			}

			categoryModel.getAvgSum().add(
					cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)) + " "
							+ cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CURRENCY)));
		}

		return categoryModel;
	}

	public void updateCategory(int id, String name, int color) {

		ContentValues values = new ContentValues();
		values.put(SQLConstants.FIELD_ID, id);
		values.put(SQLConstants.FIELD_NAME, name);
		values.put(SQLConstants.FIELD_COLOR, color);

		database.update(SQLConstants.TABLE_CATEGORY, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
	}

	private static final String GET_CATEGORIES_SUM_AMOUNT = "SELECT " + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + ","
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_COLOR + "," + "sum(" + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_AMOUNT + ") as " + SQLConstants.FIELD_SUM_AMOUNT + "," + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_CODE + " FROM " + SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE + ","
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_EXPENSE_CATEGORY
			+ "." + SQLConstants.FIELD_CATEGORY_ID + "=" + SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + "=" + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_EXPENSE_ID + " AND " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_DATE + " BETWEEN ? AND ? " + " GROUP BY " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_CATEGORY_ID;

	public Cursor getCategoiesCurrentPeriodSumAmount() {
		return database.rawQuery(GET_CATEGORIES_SUM_AMOUNT, new String[] { SettingsManager.getCurrentPeriodDates().getStartDate(),
				SettingsManager.getCurrentPeriodDates().getEndDate() });
	}

	public List<CategoryModel> parseCategoiesSumAmount(Cursor cursor) {
		List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			CategoryModel categoryModel = new CategoryModel();
			categoryModel.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
			categoryModel.setColor(cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_COLOR)));
			categoryModel.setSumAmount(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_SUM_AMOUNT)));
			categoryModel.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
			categoryModels.add(categoryModel);
		}

		return categoryModels;
	}
}
