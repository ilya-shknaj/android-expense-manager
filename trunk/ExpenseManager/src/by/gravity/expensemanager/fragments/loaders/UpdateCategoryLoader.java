package by.gravity.expensemanager.fragments.loaders;

import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.util.EmptyCursor;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class UpdateCategoryLoader extends CursorLoader {

	private int id;

	private String name;

	private int color;

	public UpdateCategoryLoader(Context context, int id, String name, int color) {

		super(context);
		this.id = id;
		this.name = name;
		this.color = color;
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().updateCategory(id, name, color);
		return new EmptyCursor();
	}

}
