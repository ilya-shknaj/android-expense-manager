package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.CheckedTextView;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.util.Constants;

public class ChooseCurrencyAdapter extends ResourceCursorAdapter {

	public ChooseCurrencyAdapter(Context context, Cursor c) {

		super(context, android.R.layout.simple_list_item_multiple_choice, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		String name = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
		String code = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));
		CheckedTextView checkBox = (CheckedTextView) view;

		checkBox.setText(String.format(Constants.CURRENCY_FORMAT, name, code));

	}

}
