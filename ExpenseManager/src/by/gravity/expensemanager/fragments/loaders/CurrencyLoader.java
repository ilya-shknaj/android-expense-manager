package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class CurrencyLoader extends CursorLoader {

	private boolean loadOnlyShortCurrencies;

	public CurrencyLoader(Context context, boolean loadOnlyShortCurrencies) {
		super(context);
		this.loadOnlyShortCurrencies = loadOnlyShortCurrencies;
	}

	@Override
	public Cursor loadInBackground() {
		if (loadOnlyShortCurrencies) {
			return SQLDataManager.getInstance().getCurrenciesShortCursor();
		} else {
			return SQLDataManager.getInstance().getCurrenciesFullCursor();
		}

	}

}
