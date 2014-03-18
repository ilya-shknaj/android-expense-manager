package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class DeletePaymentMethodLoader extends CursorLoader {

	private long paymentMethodId;

	public DeletePaymentMethodLoader(Context context, long paymentMethodId) {
		super(context);
		this.paymentMethodId = paymentMethodId;
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().deletePaymentMethod(paymentMethodId);
		return null;
	}

}
