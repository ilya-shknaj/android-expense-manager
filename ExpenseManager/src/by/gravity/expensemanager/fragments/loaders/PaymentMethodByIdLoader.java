package by.gravity.expensemanager.fragments.loaders;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class PaymentMethodByIdLoader extends CursorLoader {

	private Long paymentMethodId;

	public PaymentMethodByIdLoader(Context context, Long paymentMethodId) {
		super(context);
		this.paymentMethodId = paymentMethodId;
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getPaymentMethod(paymentMethodId);
	}

}
