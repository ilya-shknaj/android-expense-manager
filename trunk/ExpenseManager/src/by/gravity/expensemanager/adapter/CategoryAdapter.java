package by.gravity.expensemanager.adapter;

import by.gravity.expensemanager.data.SQLDataManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

public class CategoryAdapter extends ResourceCursorAdapter {

	public CategoryAdapter(Context context, Cursor c) {

		super(context, android.R.layout.simple_list_item_1, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		String categoryName = SQLDataManager.getInstance().parseCategoriesCursor(cursor);
		textView.setText(categoryName);

	}

}
