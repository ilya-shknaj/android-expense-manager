package by.gravity.expensemanager.fragments.loaders.outcome;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class ChildGroupedByCategoryNameLoader extends CursorLoader {

	private int categoryId;

	public ChildGroupedByCategoryNameLoader(Context context, int categoryId) {
		super(context);
		this.categoryId = categoryId;
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getGroupedByCategoryNameChildCursor(categoryId);
	}
}
