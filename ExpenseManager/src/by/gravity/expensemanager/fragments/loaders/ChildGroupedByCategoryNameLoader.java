package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class ChildGroupedByCategoryNameLoader extends CursorLoader {

	private long categoryId;

	public ChildGroupedByCategoryNameLoader(Context context, long categoryId) {
		super(context);
		this.categoryId = categoryId;
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getGroupedByCategoryNameChildCursor(categoryId);
	}
}
