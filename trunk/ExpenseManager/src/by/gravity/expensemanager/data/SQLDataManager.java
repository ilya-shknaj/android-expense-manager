package by.gravity.expensemanager.data;

import java.text.SimpleDateFormat;
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
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.data.helper.SQLDataManagerHelper;
import by.gravity.expensemanager.util.Constants;

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

	public void addExpense(final String amount, final String currency, final Date date, final Date time, final List<String> categories,
			final String note, final String paymentMethod) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				updateCategoriesAsync(categories);

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, getCurrencyIdAsync(currency));
				values.put(SQLConstants.FIELD_DATE, date.getTime());
				values.put(SQLConstants.FIELD_TIME, time.getTime());
				if (!StringUtil.isEmpty(note)) {
					values.put(SQLConstants.FIELD_NOTE, note);
				}
				Long paymentMethodId = getPaymentMethodIdAsync(paymentMethod);
				if (paymentMethodId != null) {
					values.put(SQLConstants.FIELD_PAYMENT_METHOD, paymentMethodId);
				}
				long expenseId = database.insert(SQLConstants.TABLE_EXPENSE, null, values);
				if (categories.size() > 0) {
					addExpenseCategoriesAsync(categories, expenseId);
					updateUsageCategoryCountAsync(categories);
				}
				return null;

			}
		}.start();
	}

	public void getCurrenciesShort(final OnLoadCompleteListener loadCompleteListener) {
		new AsyncTask<Void, Void, List<String>>(loadCompleteListener) {

			@Override
			protected List<String> doInBackground(Void... params) {
				Cursor cursor = database.query(SQLConstants.TABLE_CURRENCY, null, SQLConstants.FIELD_IS_SHOW + "=?", new String[] { "1" }, null,
						null, null);
				List<String> currencyList = new ArrayList<String>();
				if (cursor != null && cursor.getCount() > 0) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						currencyList.add(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
					}

					cursor.close();
				}
				return currencyList;
			}

		}.start();
	}

	public void getPaymentsMethodsShort(final OnLoadCompleteListener onLoadCompleteListener) {
		new AsyncTask<Void, Void, List<String>>(onLoadCompleteListener) {

			@Override
			protected List<String> doInBackground(Void... params) {
				Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_NAME }, null, null, null, null,
						null);
				List<String> paymentMethodsList = new ArrayList<String>();
				if (cursor != null && cursor.getCount() > 0) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						paymentMethodsList.add(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
					}
					cursor.close();
				}
				return paymentMethodsList;
			}

		}.start();
	}

	public void getCategoriesPopular(final OnLoadCompleteListener loadCompleteListener) {
		new AsyncTask<Void, Void, List<String>>(loadCompleteListener) {

			@Override
			protected List<String> doInBackground(Void... params) {
				Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, null, null, null, null, null, SQLConstants.FIELD_USAGE_COUNT + " DESC");
				List<String> categories = new ArrayList<String>();
				if (cursor != null && cursor.getCount() > 0) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						categories.add(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
					}

					cursor.close();
				}

				return categories;
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
				+ makePlaceholders(categories.size()) + ") COLLATE NOCASE", categories.toArray(new String[] {}), null, null, null);
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

	private void updateCategoriesAsync(final List<String> categories) {
		for (int i = 0; i < categories.size(); i++) {
			String category = categories.get(i);
			if (getCategoryIdAsync(category) == null) {
				addCategoryAsync(StringUtil.uppercaseFirstLetter(category));
			}
		}
	}

	private void addExpenseCategoriesAsync(final List<String> categories, final long expenseId) {
		List<Long> categoriesId = getCategoriesIdAsync(categories);
		ContentValues values = null;
		for (int i = 0; i < categoriesId.size(); i++) {
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

	private void updateUsageCategoryCountAsync(final List<String> categories) {
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
}
