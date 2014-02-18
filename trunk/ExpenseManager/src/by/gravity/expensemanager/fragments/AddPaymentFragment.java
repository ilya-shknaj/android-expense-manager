package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.model.PaymentDetail;
import by.gravity.expensemanager.model.PaymentModel;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class AddPaymentFragment extends CommonSherlockFragment {

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
					currentText.append("1");
				} else if (viewId == R.id.two) {
					currentText.append("2");
				} else if (viewId == R.id.three) {
					currentText.append("3");
				} else if (viewId == R.id.four) {
					currentText.append("4");
				} else if (viewId == R.id.five) {
					currentText.append("5");
				} else if (viewId == R.id.six) {
					currentText.append("6");
				} else if (viewId == R.id.seven) {
					currentText.append("7");
				} else if (viewId == R.id.eight) {
					currentText.append("8");
				} else if (viewId == R.id.nine) {
					currentText.append("9");
				} else if (viewId == R.id.doubleZero) {
					currentText.append("00");
				} else if (viewId == R.id.zero) {
					currentText.append("0");
				} else if (viewId == R.id.dot) {
					currentText.append(".");
				} else if (viewId == R.id.backspace) {
					if (currentText.length() > 0) {
						currentText.deleteCharAt(currentText.length() - 1);
					}
				} else if (viewId == R.id.cancelButton) {
					dialog.dismiss();
				}

				numberTextView.setText(StringUtil.convertNumberToHumanFriednly(currentText.toString().replace(" ", "")));
				if (viewId == R.id.okButton) {
					EditText costEditText = (EditText) getView().findViewById(R.id.cost);
					costEditText.setText(numberTextView.getText());
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
		dialog.setContentView(dialogLayout);
		dialog.setCancelable(true);
		dialog.show();

	}

	private void initBottomTabBar() {
		View cancelButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

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
		Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getCurrencies());

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void initPaymentsMethods() {

		RadioGroup paymentsMethodGroup = (RadioGroup) getView().findViewById(R.id.paymentMethodsGroup);

		paymentsMethodGroup.removeAllViews();
		PaymentModel paymentModel = getPaymentModel();

		for (int i = 0; i < paymentModel.getListPaymensDetail().size(); i++) {
			RadioButton radioButton = new RadioButton(getActivity());
			radioButton.setText(paymentModel.getListPaymensDetail().get(i).getName());
			paymentsMethodGroup.addView(radioButton, i);
		}

	}

	private void initDate() {
		TextView dateTextView = (TextView) getView().findViewById(R.id.date);
		final Calendar date = getDate();
		dateTextView.setText(getFriendlyDate(date));
		dateTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showSelectDateDialog(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						date.get(Calendar.DAY_OF_MONTH), new OnDateSetListener() {

							@Override
							public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
							}
						});

			}
		});

		TextView timeTextView = (TextView) getView().findViewById(R.id.time);
		timeTextView.setText(getTime());
		timeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

			}
		});

	}

	private String getFriendlyDate(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH) + " " + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	private Calendar getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar;

	}

	private String getTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
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
					categoriesEditText.append(buttonView.getText().toString() + ", ");
				}
			}
		};
		categoriesEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count > 0) {
					checkCategory(categoriesLayout, checkedChangeListener, s.toString(), true);
				} else if (before > 0) {
					if (start > 1 && (s.charAt(start - 1) == ',' || s.charAt(start - 1) == ' ')) {
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
		int index = currentCategoryText.lastIndexOf(",");
		String word = null;
		if (index != -1) {
			if (index + 2 == currentCategoryText.length()) {
				word = currentCategoryText.substring(0, index);
				index = word.lastIndexOf(",");
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
		String[] enteredCategoryArray = enteredCategory.split(",");
		for (int i = 0; i < enteredCategoryArray.length; i++) {
			if (enteredCategoryArray[i].trim().equals(currentCategory)) {
				return true;
			}
		}

		return false;
	}

	private String getEditableCategoryTextAfterRemove(String categoryToRemove, String enteredCategory) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] enteredCategoryArray = enteredCategory.split(", ");
		for (int i = 0; i < enteredCategoryArray.length; i++) {
			if (!enteredCategoryArray[i].trim().equals(categoryToRemove)) {
				stringBuilder.append(enteredCategoryArray[i]);
				stringBuilder.append(", ");
			}
		}

		return stringBuilder.toString();
	}

	private List<String> getCurrencies() {
		List<String> currencies = new ArrayList<String>();
		currencies.add("BYR");
		currencies.add("USD");
		currencies.add("EUR");

		return currencies;
	}

	private List<String> getCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("Продукты");
		categories.add("Алми");
		categories.add("Расходы на автомобиль");
		categories.add("Коммунальные платежи");
		categories.add("Белтелеком");
		// categories.add("Products");
		// categories.add("Almi");
		// categories.add("Avto");
		// categories.add("flat");
		// categories.add("beltelecom");

		return categories;
	}

	private PaymentModel getPaymentModel() {
		PaymentModel paymentModel = new PaymentModel();
		paymentModel.getListPaymensDetail().add(new PaymentDetail("Prior зарплата", "3 200 000"));
		paymentModel.getListPaymensDetail().add(new PaymentDetail("Prior быстрый депозит", "4 200 000"));
		paymentModel.getListPaymensDetail().add(new PaymentDetail("Наличные", "600 000"));

		paymentModel.setBalance("8 000 000");

		return paymentModel;
	}

	@Override
	public int getViewId() {
		return R.layout.f_add_payment;
	}

	@Override
	public int getTitleResource() {
		return R.string.addPayment;
	}

}
