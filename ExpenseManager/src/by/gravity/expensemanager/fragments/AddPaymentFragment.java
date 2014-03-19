package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import by.gravity.common.task.OnLoadCompleteListener;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.GlobalUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.NumberDialog.OnInputCompleteListener;
import by.gravity.expensemanager.fragments.loaders.CategoriesLoader;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.DeletePaymentLoader;
import by.gravity.expensemanager.fragments.loaders.ExpenseLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper.LoaderStatus;
import by.gravity.expensemanager.fragments.loaders.PaymentMethodsLoader;
import by.gravity.expensemanager.model.ExpenseModel;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.OnPositiveButtonClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class AddPaymentFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

	private static final String TAG = AddPaymentFragment.class.getSimpleName();

	private static final String ARG_EXPENSE_ID = "ARG_PAYMENT_ID";

	private ExpenseModel expenseModel;

	public static AddPaymentFragment newInstance(Long expenseId) {
		AddPaymentFragment fragment = new AddPaymentFragment();
		Bundle bundle = new Bundle();
		if (expenseId != null) {
			bundle.putLong(ARG_EXPENSE_ID, expenseId);
		}
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		if (getExpenseId() == 0) {
			showNumberDialog(null);
		}
		startLoaders();

		initBottomTabBar();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_payment, menu);
		menu.findItem(R.id.remove).setVisible(getExpenseId() != 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.remove))) {
			DialogHelper.showConfirmDialog(getActivity(), R.string.remove, R.string.removePaymentMessage, new OnPositiveButtonClickListener() {

				@Override
				public void onPositiveButtonClicked() {
					setContentShown(false);
					LoaderHelper.getIntance().startLoader(AddPaymentFragment.this, LoaderHelper.DELETE_PAYMENT_ID, AddPaymentFragment.this);
				}
			});
		}
		return false;
	}

	private void startLoaders() {
		getLoaderManager().initLoader(LoaderHelper.ADD_PAYMENT_CURRENCIES_ID, null, this);
		getLoaderManager().initLoader(LoaderHelper.ADD_PAYMENT_PAYMENT_METHODS_ID, null, this);
		getLoaderManager().initLoader(LoaderHelper.ADD_PAYMENT_CATEGORIES_ID, null, this);

		if (getExpenseId() != 0) {
			getLoaderManager().initLoader(LoaderHelper.ADD_PAYMENT_EXPENSE_ID, null, this);
		}
	}

	private Long getExpenseId() {
		return getArguments().getLong(ARG_EXPENSE_ID);
	}

	private void parseExpenseModel(Cursor cursor) {
		if (cursor.moveToFirst()) {
			expenseModel = new ExpenseModel();
			expenseModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)));
			expenseModel.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
			expenseModel.setDate(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_DATE)));
			expenseModel.setTime(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_TIME)));
			expenseModel.setNote(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NOTE)));
			String categories = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
			expenseModel.setCategories(Arrays.asList(categories.split(Constants.COMMA_STRING)));
			expenseModel.setPaymentMethod(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_PAYMENT_METHOD)));

			cursor.close();
		}

	}

	private void initBottomTabBar() {
		final View cancelButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalUtil.hideSoftKeyboard(getActivity());
				getFragmentManager().popBackStack();
			}
		});

		final View saveButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText costEditText = (EditText) getView().findViewById(R.id.cost);
				if (StringUtil.isEmpty(costEditText.getText().toString())) {
					Toast.makeText(getActivity(), R.string.emptyAmount, Toast.LENGTH_SHORT).show();
					return;
				}

				final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(R.id.categoriesEditText);
				final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
				final TextView dateTextView = (TextView) getView().findViewById(R.id.date);
				final TextView timeTextView = (TextView) getView().findViewById(R.id.time);
				final EditText noteEditText = (EditText) getView().findViewById(R.id.note);

				Long date = (Long) dateTextView.getTag();
				Long time = (Long) timeTextView.getTag();

				if (categoriesEditText.getText().length() != 0) {
					if (getExpenseId() == 0) {
						SQLDataManager.getInstance().addExpense(costEditText.getText().toString(), spinner.getSelectedItem().toString(), date, time,
								getEnteredCategoriesList(categoriesEditText.getText().toString()), noteEditText.getText().toString(),
								getPaymentMethod(), new OnLoadCompleteListener<Boolean>() {

									@Override
									public void onComplete(Boolean result) {
										GlobalUtil.hideSoftKeyboard(getActivity());
										// ((MainActivity)
										// getActivity()).notifyOutcomeFragmentStateChanged();
										getFragmentManager().popBackStack();
									}
								});

					} else {
						SQLDataManager.getInstance().updateExpense(getExpenseId(), costEditText.getText().toString(),
								spinner.getSelectedItem().toString(), date, time, getEnteredCategoriesList(categoriesEditText.getText().toString()),
								noteEditText.getText().toString(), getPaymentMethod(), new OnLoadCompleteListener<Void>() {

									@Override
									public void onComplete(Void result) {
										GlobalUtil.hideSoftKeyboard(getActivity());
										getActivity().getSupportFragmentManager().popBackStack();
									}

								});
					}

				} else {
					Toast.makeText(getActivity(), R.string.emptyCategory, Toast.LENGTH_SHORT).show();
					categoriesEditText.requestFocus();
				}
			}

		});
	}

	private void initCostEditText() {
		final EditText costEditText = (EditText) getView().findViewById(R.id.cost);
		costEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean focused) {
				if (focused) {
					costEditText.clearFocus();
					showNumberDialog(costEditText.getText().toString());
				}
			}
		});
		if (expenseModel != null) {
			costEditText.setText(StringUtil.convertNumberToHumanFriednly(expenseModel.getAmount()));
		}
	}

	private void initCurrency(List<String> currencyList) {
		final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencyList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}

	private void selectExpenseCurrency() {
		final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
		if (expenseModel == null) {
			// TODO load from settings
			spinner.setSelection(0);
		} else {
			int count = spinner.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				if (spinner.getItemAtPosition(i).toString().equals(expenseModel.getCurrency())) {
					spinner.setSelection(i);
					return;
				}
			}
		}
	}

	private void initPaymentsMethods(List<String> paymentMethods) {

		final RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);

		paymentsMethodGroup.removeAllViews();
		if (paymentMethods.size() > 0) {
			for (int i = 0; i < paymentMethods.size(); i++) {
				RadioButton radioButton = new RadioButton(getActivity());
				radioButton.setText(paymentMethods.get(i));
				paymentsMethodGroup.addView(radioButton, i);
			}
		} else {
			View emptyPaymentMethods = getView().findViewById(R.id.emptyPaymentMethods);
			emptyPaymentMethods.setVisibility(View.VISIBLE);
		}

	}

	private void selectPaymentMethod() {
		final RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);
		String paymentMethod = null;
		if (expenseModel == null) {
			paymentMethod = SettingsManager.getPaymentMethod();

		} else {
			paymentMethod = expenseModel.getPaymentMethod();

		}

		if (paymentMethod.equals(getString(R.string.emptyPaymentMethods))) {
			((RadioButton) paymentsMethodGroup.getChildAt(0)).setChecked(true);
			return;
		}
		int count = paymentsMethodGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			RadioButton radioButton = (RadioButton) paymentsMethodGroup.getChildAt(i);
			if (radioButton.getText().toString().equals(paymentMethod)) {
				radioButton.setChecked(true);
				return;
			}
		}
	}

	private String getPaymentMethod() {
		final RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);
		int count = paymentsMethodGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			RadioButton radioButton = ((RadioButton) paymentsMethodGroup.getChildAt(i));
			if (radioButton.isChecked()) {
				return radioButton.getText().toString();
			}

		}

		return null;
	}

	private void initDate() {
		final TextView dateTextView = (TextView) getView().findViewById(R.id.date);
		final Calendar date = getCurrentDate();
		dateTextView.setTag(date.getTimeInMillis());
		dateTextView.setText(getFriendlyDate(date));
		dateTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showSelectDateDialog(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						date.get(Calendar.DAY_OF_MONTH), new OnDateSetListener() {

							@Override
							public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
								date.set(Calendar.YEAR, year);
								date.set(Calendar.MONTH, month);
								date.set(Calendar.DAY_OF_MONTH, day);
								date.set(Calendar.HOUR_OF_DAY, 0);
								date.set(Calendar.MINUTE, 0);
								date.set(Calendar.SECOND, 0);
								date.set(Calendar.MILLISECOND, 0);
								dateTextView.setTag(date.getTimeInMillis());
								dateTextView.setText(getFriendlyDate(year, month, day));
							}
						});

			}
		});
		if (expenseModel != null) {
			date.setTimeInMillis(expenseModel.getDate());
			dateTextView.setText(getFriendlyDate(date));
		}

		final TextView timeTextView = (TextView) getView().findViewById(R.id.time);
		final Calendar time = getCurrentTime();
		timeTextView.setText(StringUtil.getFriendltyTime(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
		timeTextView.setTag(time.getTimeInMillis());
		timeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showTimePickerDialog(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
								time.setTime(date.getTime());
								time.set(Calendar.HOUR_OF_DAY, hourOfDay);
								time.set(Calendar.MINUTE, minute);
								timeTextView.setTag(time.getTimeInMillis());
								timeTextView.setText(StringUtil.getFriendltyTime(hourOfDay, minute));
							}
						});
			}
		});
		if (expenseModel != null) {
			time.setTimeInMillis(expenseModel.getTime());
			timeTextView.setText(StringUtil.getFriendltyTime(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
		}

	}

	private void initNote() {
		EditText note = (EditText) getView().findViewById(R.id.note);
		if (expenseModel != null && !StringUtil.isEmpty(expenseModel.getNote())) {
			note.setText(expenseModel.getNote());
		}
	}

	private String getFriendlyDate(Calendar calendar) {
		return CalendarUtil.getDay(calendar.get(Calendar.DAY_OF_MONTH)) + Constants.SPACE_STRING
				+ CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	private String getFriendlyDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return getFriendlyDate(calendar);
	}

	private Calendar getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;

	}

	private Calendar getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar;
	}

	private void initCategories(List<String> categories) {
		final LinearLayout categoriesLayout = (LinearLayout) getView().findViewById(R.id.categoriesLayout);
		final List<String> allCategoriesList = categories;
		final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(R.id.categoriesEditText);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, allCategoriesList);
		categoriesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		categoriesEditText.setAdapter(adapter);
		int showCategoriesCount = Integer.parseInt(SettingsManager.getCategoriesShowCount());
		final List<String> popularCategories = allCategoriesList.size() > showCategoriesCount ? allCategoriesList.subList(0, showCategoriesCount)
				: allCategoriesList;
		final OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (hasEnteredCategory(buttonView.getText().toString(), categoriesEditText.getText().toString())) {
					categoriesEditText.setText(getEditableCategoryTextAfterRemove(buttonView.getText().toString(), categoriesEditText.getText()
							.toString()));
				} else {
					categoriesEditText.append(buttonView.getText().toString() + Constants.CATEGORY_SPLITTER);
				}
			}
		};
		categoriesEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count > 0) {
					checkCategory(categoriesLayout, checkedChangeListener, s.toString(), true);
				} else if (before > 0) {
					if (start > 1 && (s.charAt(start - 1) == Constants.COMMA_CHAR || s.charAt(start - 1) == Constants.SPACE_CHAR)) {
						return;
					}
					checkCategory(categoriesLayout, checkedChangeListener, s.toString(), false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		categoriesLayout.removeAllViews();

		for (int i = 0; i < popularCategories.size(); i++) {
			CheckBox checkBox = new CheckBox(getActivity());
			checkBox.setText(popularCategories.get(i));
			checkBox.setTag(popularCategories.get(i).toLowerCase());
			checkBox.setOnCheckedChangeListener(checkedChangeListener);
			categoriesLayout.addView(checkBox, i);
		}

		final View showAllCategoriesButton = getView().findViewById(R.id.showAllCategoriesButton);
		if (allCategoriesList.size() > popularCategories.size()) {
			showAllCategoriesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					List<String> enteredCategories = getEnteredCategoriesList(categoriesEditText.getText().toString());
					for (int i = popularCategories.size(); i < allCategoriesList.size(); i++) {
						CheckBox checkBox = new CheckBox(getActivity());
						checkBox.setText(allCategoriesList.get(i));
						checkBox.setTag(allCategoriesList.get(i).toLowerCase());
						if (enteredCategories.contains(allCategoriesList.get(i))) {
							checkBox.setChecked(true);
						}
						checkBox.setOnCheckedChangeListener(checkedChangeListener);
						categoriesLayout.addView(checkBox, i);
					}
					showAllCategoriesButton.setVisibility(View.GONE);
				}
			});
		} else {
			showAllCategoriesButton.setVisibility(View.GONE);
		}

	}

	private void setExpenseCategories() {
		final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(R.id.categoriesEditText);
		for (int i = 0; i < expenseModel.getCategories().size(); i++) {
			categoriesEditText.append(expenseModel.getCategories().get(i) + Constants.CATEGORY_SPLITTER);
		}
	}

	private void checkCategory(LinearLayout categoriesLayout, OnCheckedChangeListener checkedChangeListener, String currentCategoryText, boolean check) {
		int index = currentCategoryText.lastIndexOf(Constants.COMMA_STRING);
		String word = null;
		if (index != -1) {
			if (index + 2 == currentCategoryText.length()) {
				word = currentCategoryText.substring(0, index);
				index = word.lastIndexOf(Constants.COMMA_STRING);
				if (index > 1) {
					word = word.substring(index + 1);
				}
			} else {
				word = currentCategoryText.toString().substring(index + 1);
			}

		} else {
			word = currentCategoryText.toString();
		}
		if (word.length() > 0) {
			View enteredCheckBox = categoriesLayout.findViewWithTag(word.trim().toLowerCase());
			if (enteredCheckBox != null) {
				((CheckBox) enteredCheckBox).setOnCheckedChangeListener(null);
				((CheckBox) enteredCheckBox).setChecked(check);
				((CheckBox) enteredCheckBox).setOnCheckedChangeListener(checkedChangeListener);
			}
		}
	}

	private boolean hasEnteredCategory(String currentCategory, String enteredCategoryText) {
		String[] enteredCategoryArray = getEnteredCategories(enteredCategoryText);
		for (int i = 0; i < enteredCategoryArray.length; i++) {
			if (enteredCategoryArray[i].equals(currentCategory)) {
				return true;
			}
		}

		return false;
	}

	private String[] getEnteredCategories(String enteredCategoryText) {
		if (enteredCategoryText.length() > 0) {
			return enteredCategoryText.replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING).split(Constants.COMMA_STRING);
		}

		return new String[] {};

	}

	private List<String> getEnteredCategoriesList(String enteredCategoryText) {
		return Arrays.asList(getEnteredCategories(enteredCategoryText));
	}

	private String getEditableCategoryTextAfterRemove(String categoryToRemove, String enteredCategory) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] enteredCategoryArray = enteredCategory.split(Constants.CATEGORY_SPLITTER);
		for (int i = 0; i < enteredCategoryArray.length; i++) {
			if (!enteredCategoryArray[i].trim().equals(categoryToRemove)) {
				stringBuilder.append(enteredCategoryArray[i]);
				stringBuilder.append(Constants.CATEGORY_SPLITTER);
			}
		}

		return stringBuilder.toString();
	}

	@Override
	public int getViewId() {
		return R.layout.f_add_payment;
	}

	@Override
	public int getTitleResource() {
		return R.string.addPayment;
	}

	private void showNumberDialog(String costs) {
		NumberDialog numberDialog = NumberDialog.newInstance(costs, new OnInputCompleteListener() {

			@Override
			public void onInputCompleted(String value) {
				EditText costEditText = (EditText) getView().findViewById(R.id.cost);
				costEditText.setText(value);
			}
		});
		numberDialog.show(getFragmentManager(), Number.class.getSimpleName());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		LoaderHelper.getIntance().putLoaderStatus(TAG, id, LoaderStatus.STARTED);
		if (id == LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) {
			return new CurrencyLoader(getActivity(), true);
		} else if (id == LoaderHelper.ADD_PAYMENT_PAYMENT_METHODS_ID) {
			return new PaymentMethodsLoader(getActivity());
		} else if (id == LoaderHelper.ADD_PAYMENT_CATEGORIES_ID) {
			return new CategoriesLoader(getActivity());
		} else if (id == LoaderHelper.ADD_PAYMENT_EXPENSE_ID) {
			return new ExpenseLoader(getActivity(), getExpenseId());
		} else if (id == LoaderHelper.DELETE_PAYMENT_ID) {
			return new DeletePaymentLoader(getActivity(), getExpenseId());
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) {
			List<String> currencyList = parseCurrency(cursor);
			initCurrency(currencyList);
		} else if (loader.getId() == LoaderHelper.ADD_PAYMENT_PAYMENT_METHODS_ID) {
			List<String> paymentMethodsList = parsePaymentMethods(cursor);
			initPaymentsMethods(paymentMethodsList);
		} else if (loader.getId() == LoaderHelper.ADD_PAYMENT_CATEGORIES_ID) {
			List<String> categoriesList = parseCategories(cursor);
			initCategories(categoriesList);
		} else if (loader.getId() == LoaderHelper.ADD_PAYMENT_EXPENSE_ID) {
			parseExpenseModel(cursor);
		} else if (loader.getId() == LoaderHelper.DELETE_PAYMENT_ID) {
			((MainActivity) getActivity()).delayedPopBackStack();
		}

		if (isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) && isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_CATEGORIES_ID)
				&& isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_PAYMENT_METHODS_ID)
				&& (getExpenseId() == 0 ? true : isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_EXPENSE_ID))) {
			initCostEditText();
			initDate();
			initNote();
			selectExpenseCurrency();
			selectPaymentMethod();
			if (expenseModel != null) {
				setExpenseCategories();
			}
			setContentShown(true);
		}

	}

	private List<String> parseCurrency(Cursor cursor) {
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

	private List<String> parsePaymentMethods(Cursor cursor) {
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

	private List<String> parseCategories(Cursor cursor) {
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		LoaderHelper.getIntance().clearLoaderStatus(TAG);
	}

}
