package by.gravity.expensemanager.fragments;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
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
import by.gravity.expensemanager.fragments.NumberDialog.OnInputCompleteListener;
import by.gravity.expensemanager.util.Constants;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class AddPaymentFragment extends CommonSherlockFragment {

	private static final int SHOW_CATEGORIES_COUNT = 3;

	public static AddPaymentFragment newInstance() {
		return new AddPaymentFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showNumberDialog(null);
		initCostEditText();
		initCurrency();
		initPaymentsMethods();
		initCategories();
		initDate();
		initBottomTabBar();
	}

	private void initBottomTabBar() {
		final View cancelButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalUtil.hideSoftKeyboard(getActivity());
				getActivity().getSupportFragmentManager().popBackStack();
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

				final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(
						R.id.categoriesEditText);
				final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
				final TextView dateTextView = (TextView) getView().findViewById(R.id.date);
				final TextView timeTextView = (TextView) getView().findViewById(R.id.time);
				final EditText noteEditText = (EditText) getView().findViewById(R.id.note);

				Long date = (Long) dateTextView.getTag();
				Long time = (Long) timeTextView.getTag();

				SQLDataManager.getInstance().addExpense(costEditText.getText().toString(), spinner.getSelectedItem().toString(), date,
						time, getEnteredCategoriesList(categoriesEditText.getText().toString()), noteEditText.getText().toString(), null,
						new OnLoadCompleteListener<Boolean>() {

							@Override
							public void onComplete(Boolean result) {
								if (categoriesEditText.getText().length() == 0) {
									Toast.makeText(getActivity(), R.string.emptyCategory, Toast.LENGTH_SHORT).show();
									categoriesEditText.requestFocus();
								} else {
									GlobalUtil.hideSoftKeyboard(getActivity());
//									((MainActivity) getActivity()).notifyOutcomeFragmentStateChanged();
									getActivity().getSupportFragmentManager().popBackStack();
								}
							}
						});

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
	}

	private void initCurrency() {
		final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
		SQLDataManager.getInstance().getCurrenciesShort(new OnLoadCompleteListener<List<String>>() {

			@Override
			public void onComplete(List<String> result) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
						(List<String>) result);

				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);

			}
		});
	}

	private void initPaymentsMethods() {

		final RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);

		paymentsMethodGroup.removeAllViews();
		SQLDataManager.getInstance().getPaymentsMethodsShort(new OnLoadCompleteListener<List<String>>() {

			@Override
			public void onComplete(List<String> result) {
				List<String> paymentMethodsList = (List<String>) result;
				if (paymentMethodsList.size() > 0) {
					for (int i = 0; i < paymentMethodsList.size(); i++) {
						RadioButton radioButton = new RadioButton(getActivity());
						radioButton.setText(paymentMethodsList.get(i));
						paymentsMethodGroup.addView(radioButton, i);
					}
				} else {
					View emptyPaymentMethods = getView().findViewById(R.id.emptyPaymentMethods);
					emptyPaymentMethods.setVisibility(View.VISIBLE);
				}

			}
		});

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
								date.set(Calendar.HOUR, 0);
								date.set(Calendar.MINUTE, 0);
								date.set(Calendar.SECOND, 0);
								date.set(Calendar.MILLISECOND, 0);
								dateTextView.setTag(date.getTimeInMillis());
								dateTextView.setText(getFriendlyDate(year, month, day));
							}
						});

			}
		});

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

	}

	private String getFriendlyDate(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH) + Constants.SPACE_STRING + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	private String getFriendlyDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return getFriendlyDate(calendar);
	}

	private Calendar getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;

	}

	private Calendar getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar;
	}

	private void initCategories() {
		SQLDataManager.getInstance().getCategoriesPopular(new OnLoadCompleteListener<List<String>>() {

			@Override
			public void onComplete(List<String> result) {
				final LinearLayout categoriesLayout = (LinearLayout) getView().findViewById(R.id.categoriesLayout);
				final List<String> allCategoriesList = result;
				final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(
						R.id.categoriesEditText);
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
						allCategoriesList);
				categoriesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
				categoriesEditText.setAdapter(adapter);
				final List<String> popularCategories = allCategoriesList.size() > SHOW_CATEGORIES_COUNT ? allCategoriesList.subList(0,
						SHOW_CATEGORIES_COUNT) : allCategoriesList;
				final OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (hasEnteredCategory(buttonView.getText().toString(), categoriesEditText.getText().toString())) {
							categoriesEditText.setText(getEditableCategoryTextAfterRemove(buttonView.getText().toString(),
									categoriesEditText.getText().toString()));
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
		});

	}

	private void checkCategory(LinearLayout categoriesLayout, OnCheckedChangeListener checkedChangeListener, String currentCategoryText,
			boolean check) {
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
		numberDialog.show(getActivity().getSupportFragmentManager(), Number.class.getSimpleName());
	}

}
