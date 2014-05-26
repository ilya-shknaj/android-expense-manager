package by.gravity.expensemanager.fragments.loaders;

import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.util.EmptyCursor;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class AddCategoryLoader extends CursorLoader {

	private String name;

	private int color;

	public AddCategoryLoader(Context context, String name, int color) {

		super(context);
		this.name = name;
		this.color = color;
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().addCategory(name, color);
		return new EmptyCursor();

	}

}
