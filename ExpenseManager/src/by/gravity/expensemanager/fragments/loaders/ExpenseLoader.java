package by.gravity.expensemanager.fragments.loaders;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class ExpenseLoader extends CursorLoader {

	private Long expenseId;

	public ExpenseLoader(Context context, Long expenseId) {
		super(context);
		this.expenseId = expenseId;
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getExpenseByIdCursor(expenseId);
	}

}
