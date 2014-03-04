package by.gravity.expensemanager.fragments.loaders.addPayment;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class CurrencyLoader extends CursorLoader {

	public CurrencyLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getCurrenciesShortCursor();
	}

}
