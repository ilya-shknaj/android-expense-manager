package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class DeletePaymentLoader extends CursorLoader {

	private final Long paymentID;

	public DeletePaymentLoader(Context context, Long paymentId) {
		super(context);
		this.paymentID = paymentId;
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().deletePayment(paymentID);
		return null;
	}

}
