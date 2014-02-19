package by.gravity.expensemanager.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import by.gravity.common.task.AsyncTask;
import by.gravity.common.utils.ContextHolder;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.data.helper.SQLDataManagerHelper;

public class SQLDataManager {

	private SQLDataManagerHelper sqlHelper;
	private SQLiteDatabase database;

	private static SQLDataManager instance;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	public void addExpense(final String amount, final String currency, final Date date, final List<String> categories, final String note,
			final String paymentMethod) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				addCategoriesAsync(categories);

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_AMOUNT, amount);
				values.put(SQLConstants.FIELD_CURRENCY, getCurrencyIdAsync(currency));
				values.put(SQLConstants.FIELD_DATE, dateFormat.format(date));
				if (!StringUtil.isEmpty(note)) {
					values.put(SQLConstants.FIELD_NOTE, note);
				}
				Long paymentMethodId = getPaymentMethodIdAsync(paymentMethod);
				if (paymentMethodId != null) {
					values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodId);
				}
				long expenseId = database.insert(SQLConstants.TABLE_EXPENSE, null, values);
				addExpenseCategoriesAsync(categories, expenseId);
				return null;

			}
		}.start();
	}

	public void addCategory(final String name) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				addCategoryAsync(name);
				return null;
			}

		}.start();
	}

	private Long getCategoryIdAsync(final String name) {
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

	private List<Long> getCategoriesIdAsync(final List<String> categories) {
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

	private void addCategoryAsync(final String name) {
		ContentValues values = new ContentValues();
		values.put(SQLConstants.FIELD_NAME, name);
		database.insert(SQLConstants.TABLE_CATEGORY, null, values);
	}

	private void addCategoriesAsync(final List<String> categories) {
		for (int i = 0; i < categories.size(); i++) {
			String category = categories.get(i);
			if (getCategoryIdAsync(category) == null) {
				addCategoryAsync(category);
			}
		}
	}

	private void addExpenseCategoriesAsync(final List<String> categories, final long expenseId) {
		List<Long> categoriesId = getCategoriesIdAsync(categories);
		ContentValues values = null;
		for (int i = 0; i < categories.size(); i++) {
			values = new ContentValues();
			values.put(SQLConstants.FIELD_CATEGORY_ID, categoriesId.get(i));
			values.put(SQLConstants.FIELD_EXPENSE_ID, expenseId);
			database.insert(SQLConstants.TABLE_EXPENSE_CATEGORY, null, values);
		}
	}

	private Long getCurrencyIdAsync(final String currency) {
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

	private Long getPaymentMethodIdAsync(final String accountName) {
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

}
