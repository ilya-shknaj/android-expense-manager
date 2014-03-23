package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.CheckedTextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;

public class ChooseCurrencyAdapter extends ResourceCursorAdapter {

	public ChooseCurrencyAdapter(Context context, Cursor c) {

		super(context, R.layout.i_choose_currency, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		String name = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
		CheckedTextView checkBox = (CheckedTextView) view;

		checkBox.setText(name);

//		if (view.getTag() == null) {
//			view.setTag(cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_IS_USED)) == 1);
//		}
//
//		if ((Boolean) view.getTag()) {
//			checkBox.setChecked(true);
//		} else {
//			checkBox.setChecked(false);
//		}
	}

}
