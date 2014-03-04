package by.gravity.expensemanager.fragments.loaders.outcome;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class GroupedByCategoryNameCursorLoader extends CursorLoader {

	public GroupedByCategoryNameCursorLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getGroupedByCategoryNameCursor();
	}

}
