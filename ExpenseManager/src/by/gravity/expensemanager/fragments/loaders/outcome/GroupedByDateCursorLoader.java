package by.gravity.expensemanager.fragments.loaders.outcome;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;

public class GroupedByDateCursorLoader extends CursorLoader {

	public GroupedByDateCursorLoader(Context context) {
		super(context);
	}

	public GroupedByDateCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public Cursor loadInBackground() {
		return SQLDataManager.getInstance().getGroupedByDateCursor();
	}

}
