package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class GroupedByDateCursorLoader extends CursorLoader {

	public GroupedByDateCursorLoader(Context context) {
		super(context);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getGroupedByDateCursor();
	}

}
