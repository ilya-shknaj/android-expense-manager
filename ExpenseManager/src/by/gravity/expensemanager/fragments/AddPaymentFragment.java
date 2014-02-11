package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.model.PaymentDetail;
import by.gravity.expensemanager.model.PaymentModel;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class AddPaymentFragment extends CommonSherlockFragment {

	public static AddPaymentFragment newInstance() {
		return new AddPaymentFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initCurrency();
		initPaymentsMethods();
		initCategories();
		initDate();
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
		LinearLayout categoriesLayout = (LinearLayout) getView().findViewById(R.id.categoriesLayout);
		List<String> categories = getCategories();

		categoriesLayout.removeAllViews();
		for (int i = 0; i < categories.size(); i++) {
			CheckBox checkBox = new CheckBox(getActivity());
			checkBox.setText(categories.get(i));
			categoriesLayout.addView(checkBox, i);
		}
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
