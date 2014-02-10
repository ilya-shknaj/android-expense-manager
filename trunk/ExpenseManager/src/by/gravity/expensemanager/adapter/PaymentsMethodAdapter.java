package by.gravity.expensemanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import by.gravity.common.adapter.AbstractAdapter;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.PaymentModel;

public class PaymentsMethodAdapter extends AbstractAdapter<PaymentModel> {

	public PaymentsMethodAdapter(Context c, int pItemResource, List<PaymentModel> pList) {
		super(c, pItemResource, pList);
	}

	@Override
	public void init(View convertView, PaymentModel item) {
		LinearLayout paymentsMethodLayout = (LinearLayout) convertView.findViewById(R.id.paymentsMethodLayout);

		paymentsMethodLayout.removeAllViews();
		for (int i = 0; i < item.getListPaymensDetail().size(); i++) {
			RelativeLayout paymentDetailLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.i_category, null);
			TextView name = (TextView) paymentDetailLayout.findViewById(R.id.name);
			name.setText(item.getListPaymensDetail().get(i).getName());

			TextView balance = (TextView) paymentDetailLayout.findViewById(R.id.balance);
			balance.setText(item.getListPaymensDetail().get(i).getBalance());
			paymentsMethodLayout.addView(paymentDetailLayout, i);
		}
	}

}
