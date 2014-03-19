package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class ChildGroupedByDateCursorLoader extends CursorLoader {

	private Long date;

	public ChildGroupedByDateCursorLoader(Context context, Long date) {
		super(context);
		this.date = date;
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getChildGroupedByDateExpenseCursor(date);
	}

}
