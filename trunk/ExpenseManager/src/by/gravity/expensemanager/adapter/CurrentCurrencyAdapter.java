package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.util.Constants;

public class CurrentCurrencyAdapter extends ResourceCursorAdapter {

	public CurrentCurrencyAdapter(Context context, Cursor c) {

		super(context, R.layout.i_spinner_item, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView textView = (TextView) view;

		String text = String.format(Constants.CURRENCY_FORMAT, cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)),
				cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
		textView.setText(text);
	}

}
