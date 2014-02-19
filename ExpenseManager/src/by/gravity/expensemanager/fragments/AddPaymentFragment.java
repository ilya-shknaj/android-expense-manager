package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
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
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.GlobalUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SQLDataManager.OnLoadCompleteListener;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.math.Parser;
import by.gravity.expensemanager.util.math.SyntaxException;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class AddPaymentFragment extends CommonSherlockFragment {

	private static final String TAG = AddPaymentFragment.class.getSimpleName();

	private static final int SHOW_CATEGORIES_COUNT = 5;

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
		View cancelButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		View saveButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
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
		SQLDataManager.getInstance().getCurrenciesShort(new OnLoadCompleteListener() {

			@Override
			public void onComplete(Object result) {
				@SuppressWarnings("unchecked")
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, (List<String>) result);

				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);

			}
		});
	}

	private void initPaymentsMethods() {

		final RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);

		paymentsMethodGroup.removeAllViews();
		SQLDataManager.getInstance().getPaymentsMethodsShort(new OnLoadCompleteListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onComplete(Object result) {
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
		final Calendar date = getDate();
		dateTextView.setText(getFriendlyDate(date));
		dateTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showSelectDateDialog(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						date.get(Calendar.DAY_OF_MONTH), new OnDateSetListener() {

							@Override
							public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
								dateTextView.setText(getFriendlyDate(year, month, day));
							}
						});

			}
		});

		final TextView timeTextView = (TextView) getView().findViewById(R.id.time);
		timeTextView.setText(getTime());
		timeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showTimePickerDialog(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
								timeTextView.setText(getTime(hourOfDay, minute));
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

	private Calendar getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar;

	}

	private String getTime() {
		Calendar calendar = Calendar.getInstance();
		return getTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	}

	private String getTime(int hourOfDay, int minutes) {
		return hourOfDay < 10 ? Constants.ZERO_STRING + hourOfDay : hourOfDay + Constants.COLON_STRING
				+ (minutes < 10 ? Constants.ZERO_STRING + minutes : minutes);
	}

	private void initCategories() {
		final LinearLayout categoriesLayout = (LinearLayout) getView().findViewById(R.id.categoriesLayout);
		List<String> categoriesList = getCategories();
		final MultiAutoCompleteTextView categoriesEditText = (MultiAutoCompleteTextView) getView().findViewById(R.id.categoriesEditText);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, categoriesList);
		categoriesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		categoriesEditText.setAdapter(adapter);
		List<String> categories = categoriesList.size() > SHOW_CATEGORIES_COUNT ? categoriesList.subList(0, 5) : categoriesList;
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
		for (int i = 0; i < categories.size(); i++) {
			CheckBox checkBox = new CheckBox(getActivity());
			checkBox.setText(categories.get(i));
			checkBox.setTag(categories.get(i).toLowerCase());
			checkBox.setOnCheckedChangeListener(checkedChangeListener);
			categoriesLayout.addView(checkBox, i);
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

	private boolean hasEnteredCategory(String currentCategory, String enteredCategory) {
		String[] enteredCategoryArray = enteredCategory.split(Constants.COMMA_STRING);
		for (int i = 0; i < enteredCategoryArray.length; i++) {
			if (enteredCategoryArray[i].trim().equals(currentCategory)) {
				return true;
			}
		}

		return false;
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

	private List<String> getCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("Продукты");
		categories.add("Алми");
		categories.add("Расходы на автомобиль");
		categories.add("Коммунальные платежи");
		categories.add("Белтелеком");

		return categories;
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
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.d_input_number, null);
		final TextView numberTextView = (TextView) dialogLayout.findViewById(R.id.text);
		if (!StringUtil.isEmpty(costs)) {
			numberTextView.setText(costs);
		}
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int viewId = v.getId();
				StringBuilder currentText = new StringBuilder(numberTextView.getText());

				if (viewId == R.id.one) {
					currentText.append(Constants.ONE_STRING);
				} else if (viewId == R.id.two) {
					currentText.append(Constants.TWO_STRING);
				} else if (viewId == R.id.three) {
					currentText.append(Constants.THREE_STRING);
				} else if (viewId == R.id.four) {
					currentText.append(Constants.FOUR_STRING);
				} else if (viewId == R.id.five) {
					currentText.append(Constants.FIVE_STRING);
				} else if (viewId == R.id.six) {
					currentText.append(Constants.SIX_STRING);
				} else if (viewId == R.id.seven) {
					currentText.append(Constants.SEVEN_STRING);
				} else if (viewId == R.id.eight) {
					currentText.append(Constants.EIGHT_STRING);
				} else if (viewId == R.id.nine) {
					currentText.append(Constants.NINE_STRING);
				} else if (viewId == R.id.doubleZero) {
					currentText.append(Constants.DOUBLE_ZERO_STRIG);
				} else if (viewId == R.id.zero) {
					currentText.append(Constants.ZERO_STRING);
				} else if (viewId == R.id.dot) {
					if (currentText.lastIndexOf(Constants.DOT_STRING) == -1) {
						currentText.append(Constants.DOT_STRING);
					}
				} else if (viewId == R.id.backspace) {
					if (currentText.length() > 0) {
						currentText.deleteCharAt(currentText.length() - 1);
					}
				} else if (viewId == R.id.plus) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.PLUS_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.MULTIPLE_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.PLUS_CHAR);
				} else if (viewId == R.id.minus) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.MINUS_CHAR) {
						return;
					} else if (lastChar == Constants.PLUS_CHAR || lastChar == Constants.MULTIPLE_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.MINUS_CHAR);
				} else if (viewId == R.id.multiple) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.MULTIPLE_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.PLUS_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.MULTIPLE_CHAR);
				} else if (viewId == R.id.devide) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.DEVIDE_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.PLUS_CHAR || lastChar == Constants.MULTIPLE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.DEVIDE_CHAR);
				} else if (viewId == R.id.cancelButton) {
					GlobalUtil.hideSoftKeyboard(getActivity());
					dialog.dismiss();
				}
				numberTextView.setText(StringUtil.convertNumberToHumanFriednly(currentText.toString().replaceAll(Constants.SPACE_PATTERN,
						Constants.EMPTY_STRING)));
				if (viewId == R.id.okButton) {
					if (!StringUtil.isEmpty(currentText.toString())) {
						EditText costEditText = (EditText) getView().findViewById(R.id.cost);
						try {

							String parsedValue = StringUtil.convertNumberToHumanFriednly(Parser.parse(
									currentText.toString().replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING)).value());
							costEditText.setText(parsedValue);
						} catch (SyntaxException e) {
							Toast.makeText(getActivity(), R.string.parseNumberError, Toast.LENGTH_SHORT).show();
							Log.e(TAG, "error ", e);
						}
					}
					dialog.dismiss();
				}
			}
		};
		View one = dialogLayout.findViewById(R.id.one);
		View two = dialogLayout.findViewById(R.id.two);
		View three = dialogLayout.findViewById(R.id.three);
		View four = dialogLayout.findViewById(R.id.four);
		View five = dialogLayout.findViewById(R.id.five);
		View six = dialogLayout.findViewById(R.id.six);
		View seven = dialogLayout.findViewById(R.id.seven);
		View eight = dialogLayout.findViewById(R.id.eight);
		View nine = dialogLayout.findViewById(R.id.nine);
		View doubleZero = dialogLayout.findViewById(R.id.doubleZero);
		View zero = dialogLayout.findViewById(R.id.zero);
		View dot = dialogLayout.findViewById(R.id.dot);
		View okButton = dialogLayout.findViewById(R.id.okButton);
		View cancelButton = dialogLayout.findViewById(R.id.cancelButton);
		View backspace = dialogLayout.findViewById(R.id.backspace);
		View plus = dialogLayout.findViewById(R.id.plus);
		View minus = dialogLayout.findViewById(R.id.minus);
		View multiple = dialogLayout.findViewById(R.id.multiple);
		View devide = dialogLayout.findViewById(R.id.devide);
		one.setOnClickListener(onClickListener);
		two.setOnClickListener(onClickListener);
		three.setOnClickListener(onClickListener);
		four.setOnClickListener(onClickListener);
		five.setOnClickListener(onClickListener);
		six.setOnClickListener(onClickListener);
		seven.setOnClickListener(onClickListener);
		eight.setOnClickListener(onClickListener);
		nine.setOnClickListener(onClickListener);
		doubleZero.setOnClickListener(onClickListener);
		zero.setOnClickListener(onClickListener);
		dot.setOnClickListener(onClickListener);
		okButton.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
		backspace.setOnClickListener(onClickListener);
		backspace.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				numberTextView.setText("");
				return false;
			}
		});
		plus.setOnClickListener(onClickListener);
		minus.setOnClickListener(onClickListener);
		multiple.setOnClickListener(onClickListener);
		devide.setOnClickListener(onClickListener);

		dialog.setContentView(dialogLayout);
		dialog.setCancelable(true);
		dialog.show();

	}

}
