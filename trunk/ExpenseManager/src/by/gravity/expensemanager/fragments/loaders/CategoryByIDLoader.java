package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class CategoryByIDLoader extends CursorLoader {

	private final long categoryID;

	public CategoryByIDLoader(Context context, long categoryID) {

		super(context);
		this.categoryID = categoryID;
	}

	@Override
	public Cursor loadInBackground() {

		return SQLDataManager.getInstance().getCategoryByID(categoryID);
	}

}
