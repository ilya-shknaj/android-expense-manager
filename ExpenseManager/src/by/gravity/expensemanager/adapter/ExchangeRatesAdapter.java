package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.GlobalUtils;

public class ExchangeRatesAdapter extends ResourceCursorAdapter {

	private String currentCurrency;

	private double currencyRate;

	public ExchangeRatesAdapter(Context context, Cursor c, int currentPosition) {

		super(context, R.layout.i_exchange_rate, c, true);
		initCurrency(c, currentPosition);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView currency = (TextView) view.findViewById(R.id.currency);
		String name = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME));
		String code = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));

		currency.setText(String.format(Constants.CURRENCY_FORMAT, name, code));

		TextView rate = (TextView) view.findViewById(R.id.rate);

		rate.setText(GlobalUtils.getFormattedRate(currentCurrency, currencyRate, cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)),
				cursor.getDouble(cursor.getColumnIndex(SQLConstants.FIELD_RATE))));

	}

	private void initCurrency(Cursor cursor, int position) {

		cursor.moveToPosition(position);
		currentCurrency = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));
		currencyRate = cursor.getDouble(cursor.getColumnIndex(SQLConstants.FIELD_RATE));
	}

	private void setCurrencyRate() {

		for (int i = 0; i < getCursor().getCount(); i++) {
			getCursor().moveToPosition(i);
			if (getCursor().getString(getCursor().getColumnIndex(SQLConstants.FIELD_CODE)).equals(currentCurrency)) {
				currencyRate = getCursor().getDouble(getCursor().getColumnIndex(SQLConstants.FIELD_RATE));
				break;
			}
		}
	}

	public void setCurrentCurrency(String currentCurrency) {

		if (!this.currentCurrency.equals(currentCurrency)) {
			this.currentCurrency = currentCurrency;
			setCurrencyRate();
			notifyDataSetChanged();
		}
	}

	public String getCurrentCurrency() {

		return currentCurrency;
	}

	public double getCurrencyRate() {

		return currencyRate;
	}

}
