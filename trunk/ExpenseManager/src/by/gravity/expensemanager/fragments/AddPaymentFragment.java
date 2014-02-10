package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.PaymentDetail;
import by.gravity.expensemanager.model.PaymentModel;

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
