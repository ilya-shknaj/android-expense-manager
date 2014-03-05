package by.gravity.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.model.PaymentMethodDetailModel;
import by.gravity.expensemanager.model.PaymentModel;

public class MainFragment extends CommonSherlockFragment {

	public static MainFragment newInstance() {
		return new MainFragment();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPaymentsMethods();
		initBottomBar();

	}

	private void initPaymentsMethods() {
		LinearLayout paymentsMethodLayout = (LinearLayout) getView().findViewById(R.id.paymentsMethodLayout);

		paymentsMethodLayout.removeAllViews();
		PaymentModel paymentModel = getPaymentModel();

		for (int i = 0; i < paymentModel.getListPaymensDetail().size(); i++) {
			RelativeLayout paymentDetailLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.i_payments_methods_detail_pay,
					null);
			TextView name = (TextView) paymentDetailLayout.findViewById(R.id.name);
			name.setText(paymentModel.getListPaymensDetail().get(i).getName());

			TextView balance = (TextView) paymentDetailLayout.findViewById(R.id.balance);
			balance.setText(paymentModel.getListPaymensDetail().get(i).getBalance());
			paymentsMethodLayout.addView(paymentDetailLayout, i);
		}

		TextView balance = (TextView) getView().findViewById(R.id.balanceLayout).findViewById(R.id.balance);
		balance.setText(paymentModel.getBalance());
	}

	private void initBottomBar() {
		LinearLayout addPayment = (LinearLayout) getView().findViewById(R.id.addPayment);
		addPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				((MainActivity) getActivity()).showAddPaymentFragment();
			}
		});
	}

	private PaymentModel getPaymentModel() {
		PaymentModel paymentModel = new PaymentModel();
		paymentModel.getListPaymensDetail().add(new PaymentMethodDetailModel("Prior зарплата", "3 200 000"));
		paymentModel.getListPaymensDetail().add(new PaymentMethodDetailModel("Prior быстрый депозит", "4 200 000"));
		paymentModel.getListPaymensDetail().add(new PaymentMethodDetailModel("Наличные", "600 000"));

		paymentModel.setBalance("8 000 000");

		return paymentModel;
	}

	@Override
	public int getViewId() {
		return R.layout.f_main;
	}

	@Override
	public int getTitleResource() {
		return R.string.main;
	}

}
