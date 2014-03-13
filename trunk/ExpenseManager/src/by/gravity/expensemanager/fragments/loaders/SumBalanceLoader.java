package by.gravity.expensemanager.fragments.loaders;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class SumBalanceLoader extends CursorLoader {

	public SumBalanceLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getSumBalance();
	}

}
