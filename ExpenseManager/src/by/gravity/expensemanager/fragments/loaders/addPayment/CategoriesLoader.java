package by.gravity.expensemanager.fragments.loaders.addPayment;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class CategoriesLoader extends CursorLoader {

	public CategoriesLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getCategoriesCursor();
	}

}
