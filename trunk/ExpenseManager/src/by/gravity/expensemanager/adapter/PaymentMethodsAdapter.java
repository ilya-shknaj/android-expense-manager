package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.model.PaymentMethodModel;
import by.gravity.expensemanager.util.Constants;

public class PaymentMethodsAdapter extends ResourceCursorAdapter {

	public PaymentMethodsAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		PaymentMethodModel model = getPaymentMethodModel(cursor);

		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(model.getName());

		TextView balance = (TextView) view.findViewById(R.id.balance);
		balance.setText(model.getBalance() + Constants.SPACE_STRING + model.getCurrency());

	}

	private PaymentMethodModel getPaymentMethodModel(Cursor cursor) {
		PaymentMethodModel paymentDetail = new PaymentMethodModel();
		paymentDetail.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
		paymentDetail.setBalance(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_BALANCE)));
		paymentDetail.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));

		return paymentDetail;
	}

}
