package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.util.Constants;

public class ExchangeRatesAdapter extends ResourceCursorAdapter {

	public ExchangeRatesAdapter(Context context, Cursor c) {

		super(context, R.layout.i_exchange_rate, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView currency = (TextView) view.findViewById(R.id.currency);
		String name = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
		String code = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));

		currency.setText(String.format(Constants.CURRENCY_FORMAT, name, code));

		TextView rate = (TextView) view.findViewById(R.id.rate);
		rate.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex(SQLConstants.FIELD_RATE))));

	}

}
