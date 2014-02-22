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
import by.gravity.expensemanager.model.CollapsedModel;
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
			final String note, final String paymentMethod,OnLoadCompleteListener onLoadCompleteListener) {
		new AsyncTask<Void, Void, Boolean>(onLoadCompleteListener) {
			@Override
			protected Boolean doInBackground(Void... params) {
				updateCategoriesAsync(categories);

				ContentValues values = new ContentValues();
				values.put(SQLConstants.FIELD_AMOUNT, amount.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING));
				values.put(SQLConstants.FIELD_CURRENCY, getCurrencyIdAsync(currency));
				values.put(SQLConstants.FIELD_DATE, date);
				values.put(SQLConstants.FIELD_TIME, time);
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
				Cursor cursor = database.query(SQLConstants.TABLE_CURRENCY, null, SQLConstants.FIELD_IS_SHOW + "=?", new String[] { "1" },
						null, null, null);
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
				Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_NAME }, null, null,
						null, null, null);
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
				Cursor cursor = database.query(SQLConstants.TABLE_CATEGORY, null, null, null, null, null, SQLConstants.FIELD_USAGE_COUNT
						+ " DESC");
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

	public void getExpenseGroupedByDate(final OnLoadCompleteListener loadCompleteListener) {
		new AsyncTask<Void, Void, List<CollapsedModel>>() {

			@Override
			protected List<CollapsedModel> doInBackground(Void... params) {
				Cursor cursor = database.query(SQLConstants.TABLE_EXPENSE, new String[] { SQLConstants.FIELD_DATE,
						"sum(" + SQLConstants.FIELD_AMOUNT + ") AS " + SQLConstants.FIELD_SUM_AMOUNT }, null, null,
						SQLConstants.FIELD_DATE, null, null);
				List<CollapsedModel> collapsedModels = new ArrayList<CollapsedModel>();
				CollapsedModel collapsedModel = null;
				if (cursor != null && cursor.getCount() > 0) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						collapsedModel = new CollapsedModel();
						collapsedModel.setAmount(String.valueOf(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_SUM_AMOUNT))));
						collapsedModel.setDate(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_DATE)));

						collapsedModels.add(collapsedModel);
					}
					cursor.close();
				}
				return collapsedModels;
			}
		}.start();
	}

	private static final String getDayExpenseQuery = "SELECT " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID + ","
			+ SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_CODE + "," + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_TIME + "," + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_AMOUNT + ","
			+ SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_NOTE + "," + SQLConstants.TABLE_EXPENSE + "."
			+ SQLConstants.FIELD_PAYMENT_METHOD + " FROM " + SQLConstants.TABLE_EXPENSE + " INNER JOIN " + SQLConstants.TABLE_CURRENCY
			+ " ON " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY + "=" + SQLConstants.TABLE_CURRENCY + "."
			+ SQLConstants.FIELD_ID + " WHERE " + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_DATE + "=?";

	public Cursor getGroupedByDateCursor() {
		return database.query(SQLConstants.TABLE_EXPENSE + "," + SQLConstants.TABLE_CURRENCY, new String[] {
				SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_ID, SQLConstants.FIELD_DATE,
				"sum(" + SQLConstants.FIELD_AMOUNT + ") AS " + SQLConstants.FIELD_SUM_AMOUNT,
				SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_SYMBOL }, SQLConstants.TABLE_CURRENCY + "." + SQLConstants.FIELD_ID
				+ "=" + SQLConstants.TABLE_EXPENSE + "." + SQLConstants.FIELD_CURRENCY, null, SQLConstants.FIELD_DATE, null, null);
	}

	public Cursor getDayExpense(Long date) {
		return database.rawQuery(getDayExpenseQuery, new String[] { String.valueOf(date) });
	}

	public Cursor getExpenseCategories(Long expenseId) {
		return database.query(SQLConstants.TABLE_CATEGORY + "," + SQLConstants.TABLE_EXPENSE_CATEGORY,
				new String[] { SQLConstants.FIELD_NAME }, SQLConstants.TABLE_EXPENSE_CATEGORY + "." + SQLConstants.FIELD_CATEGORY_ID + "="
						+ SQLConstants.TABLE_CATEGORY + "." + SQLConstants.FIELD_ID + " AND " + SQLConstants.TABLE_EXPENSE_CATEGORY + "."
						+ SQLConstants.FIELD_EXPENSE_ID + "=?", new String[] { String.valueOf(expenseId) }, null, null, null);
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

	private void addCategoryAsync(final String name) {
		ContentValues values = new ContentValues();
		values.put(SQLConstants.FIELD_NAME, name);
		database.insert(SQLConstants.TABLE_CATEGORY, null, values);
	}

	private void updateCategoriesAsync(final List<String> categories) {
		for (int i = 0; i < categories.size(); i++) {
			if (getCategoryIdAsync(categories.get(i)) == null) {
				categories.set(i, StringUtil.uppercaseFirstLetter(categories.get(i)));
				;
				addCategoryAsync(categories.get(i));
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
		Cursor cursor = database.query(SQLConstants.TABLE_PAYMENT_METHODS, new String[] { SQLConstants.FIELD_ID }, SQLConstants.FIELD_NAME
				+ "=?", new String[] { accountName }, null, null, null);
		Long id = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID));
			cursor.close();
		}

		return id;
	}

	private void updateUsageCategoryCountAsync(final List<String> categories) {
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

				database.update(SQLConstants.TABLE_CATEGORY, values, SQLConstants.FIELD_ID + "=?", new String[] { String.valueOf(id) });
			}
			cursor.close();
		}
	}
}