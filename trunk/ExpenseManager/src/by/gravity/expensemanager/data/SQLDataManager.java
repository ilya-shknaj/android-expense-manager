package by.gravity.expensemanager.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import by.gravity.common.task.AsyncTask;
import by.gravity.common.task.OnLoadCompleteListener;
import by.gravity.common.utils.ContextHolder;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.data.helper.SQLDataManagerHelper;
import by.gravity.expensemanager.model.PeriodDate;
import by.gravity.expensemanager.util.Constants;

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
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, getCurrencyId(currency));
				values.put(SQLConstants.FIELD_DATE, date);
				values.put(SQLConstants.FIELD_TIME, time);
				if (!StringUtil.isEmpty(note)) {
					values.put(SQLConstants.FIELD_NOTE, note);
				}
				Long paymentMethodId = getPaymentMethodId(paymentMethod);
				if (paymentMethodId != null) {
					values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodId);
				}
				long expenseId = database.insert(SQLConstants.TABLE_EXPENSE, null, values);
				addExpenseCategories(categories, expenseId);
				updateUsageCategoryCount(categories);
				substractFromPaymentHistory(expenseId);
				return null;

			}
		}.start();
	}

	public void updateExpense(final long id, final String amount, final String currency, final Long date, final Long time,
			final List<String> categories, final String note, final String paymentMethod, final OnLoadCompleteListener<Void> onCompletionListener) {
		new AsyncTask<Void, Void, Void>(onCompletionListener) {

			@Override
			protected Void doInBackground(Void... params) {
				updateCategories(categories);
				addToPaymentHistory(id);

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, getCurrencyId(currency));
				values.put(SQLConstants.FIELD_DATE, date);
				values.put(SQLConstants.FIELD_TIME, time);
				values.put(SQLConstants.FIELD_NOTE, note);

				long paymentMethodId = getPaymentMethodId(paymentMethod);
				values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodId);

				long expenseId = database.update(SQLConstants.TABLE_EXPENSE, values, SQLConstants.FIELD_ID + "=?",
						new String[] { String.valueOf(id) });
				deleteExpenseCategories(expenseId);
				addExpenseCategories(categories, expenseId);
				substractFromPaymentHistory(id);

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

				database.update(SQLConstants.TABLE_PAYMENT_METHODS, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
				return null;
			}
		}.start();
	}

	public Cursor getCurrenciesShortCursor() {
		return database.query(SQLConstants.TABLE_CURRENCY, null, SQLConstants.FIELD_IS_SHOW + "=?", new String[] { "1" }, null, null, null);
	}

	public Cursor getCategoriesCursor() {
		return database.query(SQLConstants.TABLE_CATEGORY, null, null, null, null, null, SQLConstants.FIELD_USAGE_COUNT + " DESC");
	}

	private static final String EXPENSE_GROUPED_BY_DATE_AND_AMOUNT_QUERY = "SELECT " + SQLConstants.FIELD_ID + "," + SQLConstants.FIELD_DATE
			+ ", group_concat(" + SQLConstants.FIELD_SUM_AMOUNT + ", '') AS " + SQLConstants.FIELD_SUM_AMOUNT + " FROM(SELECT "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + "," + SQLConstants.FIELD_DATE + ", sum(" + SQLConstants.FIELD_AMOUNT
			+ ") || ' '|| " + SQLConstants.FIELD_SYMBOL + " || '" + Constants.NEW_STRING + "' " + SQLConstants.FIELD_SUM_AMOUNT + " FROM "
			+ SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " GROUP BY " + SQLConstants.FIELD_DATE + "," + SQLConstants.FIELD_CURRENCY
			+ ")WHERE " + SQLConstants.FIELD_DATE + " BETWEEN " + "? AND ? GROUP BY " + SQLConstants.FIELD_DATE;

	public Cursor getGroupedByDateCursor() {
		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(EXPENSE_GROUPED_BY_DATE_AND_AMOUNT_QUERY, new String[] { periodDate.getStartDate(), periodDate.getEndDate() });
	}

	private static final String EXPENSE_GROUPED_BY_CATEGORY_NAME_QUERY = "SELECT " + SQLConstants.FIELD_ID + "," + SQLConstants.FIELD_NAME + ","
			+ "group_concat(" + SQLConstants.FIELD_SUM_AMOUNT + ",'') AS " + SQLConstants.FIELD_SUM_AMOUNT + " FROM( SELECT  "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + " AS " + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_NAME + "," + "sum(" + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_AMOUNT + ") || ' '||  " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_SYMBOL + "|| '"
			+ Constants.NEW_STRING + "' AS " + SQLConstants.FIELD_SUM_AMOUNT + " FROM " + SQLConstants.TABLE_EXPENSE + ","
			+ SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + " WHERE "
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID
			+ " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "=" + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_EXPENSE_ID + "="
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_DATE
			+ " BETWEEN ? AND ? GROUP BY " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "," + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_NAME + ")GROUP BY " + SQLConstants.FIELD_NAME;

	public Cursor getGroupedByCategoryNameCursor() {
		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(EXPENSE_GROUPED_BY_CATEGORY_NAME_QUERY, new String[] { periodDate.getStartDate(), periodDate.getEndDate() });
	}

	private static final String GET_CHILD_GROUPED_BY_DATE_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_TIME + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_AMOUNT
			+ " || ' ' || " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " AS " + SQLConstants.FIELD_AMOUNT + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + "," + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME
			+ " AS " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + " WHERE " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID
			+ "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_PAYMENT_METHOD + " AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_DATE + "=?";

	public Cursor getChildGroupedByDateExpenseCursor(Long date) {
		return database.rawQuery(GET_CHILD_GROUPED_BY_DATE_QUERY, new String[] { String.valueOf(date) });
	}

	private static final String GET_EXPENSE_BY_ID_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.FIELD_TIME + "," + SQLConstants.FIELD_DATE + "," + SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_CODE + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + "," + SQLConstants.TABLE_PAYMENT_METHODS
			+ "." + SQLConstants.FIELD_NAME + " AS " + SQLConstants.FIELD_PAYMENT_METHOD + ", group_concat(" + SQLConstants.TABLE_CATEGORY + "."
			+ SQLConstants.FIELD_NAME + ",',') AS " + SQLConstants.FIELD_NAME + " FROM " + SQLConstants.TABLE_EXPENSE + ","
			+ SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + "," + SQLConstants.TABLE_CATEGORY + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + " WHERE " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "="
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + "=?"
			+ " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_EXPENSE_ID + "=" + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "="
			+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_PAYMENT_METHOD + "=" + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID;

	public Cursor getExpenseByIdCursor(Long id) {
		return database.rawQuery(GET_EXPENSE_BY_ID_QUERY, new String[] { String.valueOf(id) });
	}

	private static final String GET_CATEGORY_EXPENSE_QUERY = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_DATE + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_TIME + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_AMOUNT + " || ' ' || " + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_SYMBOL + " AS " + SQLConstants.FIELD_AMOUNT + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + " AS " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM "
			+ SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + "="
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_PAYMENT_METHOD + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
			+ SQLConstants.FIELD_EXPENSE_ID + "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "= ? AND " + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE
			+ "." + SQLConstants.FIELD_DATE + " BETWEEN ? AND ?";

	public Cursor getGroupedByCategoryNameChildCursor(int category) {
		PeriodDate periodDate = SettingsManager.getCurrentPeriodDates();
		return database.rawQuery(GET_CATEGORY_EXPENSE_QUERY,
				new String[] { String.valueOf(category), periodDate.getStartDate(), periodDate.getEndDate() });
	}

	public Cursor getGrouperdByCategoryNameCategoriesCursor(long expenseId) {
		return database.query(SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY, new String[] { SQLConstants.FIELD_NAME },
				SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "=" + SQLConstants.TABLE_CATEGORY + "."
						+ SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_EXPENSE_ID + "=?",
				new String[] { String.valueOf(expenseId) }, null, null, null);
	}

	private static final String GET_PAYMENT_METHODS_QUERY = "SELECT " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + "," + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_BALANCE + "," + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " FROM "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID;

	public Cursor getPaymentMethods() {
		return database.rawQuery(GET_PAYMENT_METHODS_QUERY, null);
	}

	private static final String GET_PAYMENT_METHODS_BY_ID_QUERY = "SELECT " + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_NAME + "," + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_BALANCE + "," + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + "," + SQLConstants.FIELD_NOTE
			+ " FROM " + SQLConstants.TABLE_PAYMENT_METHODS + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS
			+ "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " AND "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_ID + "=?";

	public Cursor getPaymentMethod(Long id) {
		return database.rawQuery(GET_PAYMENT_METHODS_BY_ID_QUERY, new String[] { String.valueOf(id) });
	}

	private static final String GET_SUM_BALANCE = "SELECT group_concat(" + SQLConstants.FIELD_BALANCE + ",'" + Constants.NEW_STRING + "') AS "
			+ SQLConstants.FIELD_BALANCE + " FROM ( SELECT sum(" + SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_BALANCE
			+ ") || ' ' || " + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + " AS " + SQLConstants.FIELD_BALANCE + " FROM "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "," + SQLConstants.TABLE_CURRENCY + " WHERE " + SQLConstants.TABLE_PAYMENT_METHODS + "."
			+ SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID + " GROUP BY "
			+ SQLConstants.TABLE_PAYMENT_METHODS + "." + SQLConstants.FIELD_CURRENCY + ")";

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
		Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_NAME + " IN("
				+ makePlaceholders(categories.size()) + ")", categories.toArray(new String[] {}), null, null, null);
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

	private void addCategory(final String name) {
		ContentValues values = new ContentValues();
		values.put(SQLConstants.FIELD_NAME, name);
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
		database.delete(SQLConstants.TABLE_EXPENSE_CATEGORY, SQLConstants.FIELD_EXPENSE_ID + "=?", new String[] { String.valueOf(expenseId) });
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

	private Long getPaymentMethodId(final String accountName) {
		if (StringUtil.isEmpty(accountName)) {
			return null;
		}
		Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_NAME + "=?",
				new String[] { accountName }, null, null, null);
		Long id = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));
			cursor.close();
		}

		return id;
	}

	private void updateUsageCategoryCount(final List<String> categories) {
		Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, null, SQLConstants.FIELD_NAME + " IN(" + makePlaceholders(categories.size())
				+ ")", categories.toArray(new String[] {}), null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				long id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));
				int usageCount = cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_USAGE_COUNT));

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_USAGE_COUNT, ++usageCount);

				database.update(SQLConstants.TABLE_CATEGORY, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
			}
			cursor.close();
		}
	}

	public void deletePayment(Long id) {
		addToPaymentHistory(id);
		database.delete(SQLConstants.TABLE_EXPENSE, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
	}

	private static final String SUBSTRACT_FROM_PAYMENT_METHOD_QUERY = "UPDATE " + SQLConstants.TABLE_PAYMENT_METHODS + " SET "
			+ SQLConstants.FIELD_BALANCE + "=" + SQLConstants.FIELD_BALANCE + " - (SELECT " + SQLConstants.FIELD_AMOUNT + " FROM "
			+ SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID + "= ?) WHERE " + SQLConstants.FIELD_ID + "= (SELECT "
			+ SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID + "= ?)";

	public void substractFromPaymentHistory(Long paymentId) {
		// TODO use this method not recomended
		database.execSQL(SUBSTRACT_FROM_PAYMENT_METHOD_QUERY, new String[] { String.valueOf(paymentId), String.valueOf(paymentId) });
	}

	private static final String ADD_TO_PAYMENT_METHOD_QUERY = "UPDATE " + SQLConstants.TABLE_PAYMENT_METHODS + " SET " + SQLConstants.FIELD_BALANCE
			+ "=" + SQLConstants.FIELD_BALANCE + " + (SELECT " + SQLConstants.FIELD_AMOUNT + " FROM " + SQLConstants.TABLE_EXPENSE + " WHERE "
			+ SQLConstants.FIELD_ID + "= ?) WHERE " + SQLConstants.FIELD_ID + "= (SELECT " + SQLConstants.FIELD_PAYMENT_METHOD + " FROM "
			+ SQLConstants.TABLE_EXPENSE + " WHERE " + SQLConstants.FIELD_ID + "= ?)";

	public void addToPaymentHistory(Long paymentId) {
		// TODO use this method not recomended
		database.execSQL(ADD_TO_PAYMENT_METHOD_QUERY, new String[] { String.valueOf(paymentId), String.valueOf(paymentId) });
	}
}
