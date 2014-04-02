package by.gravity.expensemanager.fragments;

import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.CurrentCurrencyAdapter;
import by.gravity.expensemanager.adapter.ExchangeRatesAdapter;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;

public class ExchangeRatesFragment extends CommonProgressSherlockFragment {

	public static ExchangeRatesFragment newInstance() {

		ExchangeRatesFragment fragment = new ExchangeRatesFragment();

		return fragment;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {

		if (id == LoaderHelper.CURRENCIES) {
			return new CurrencyLoader(getActivity(), false);
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.CURRENCIES) {
			initListView(cursor);
			setContentShown(true);
		}

	}

	private void initListView(final Cursor cursor) {

		ListView listView = (ListView) getView().findViewById(R.id.listView);
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.i_current_currency, null);
		Spinner spinner = (Spinner) headerView.findViewById(R.id.spinner);
		int position = getCurrencyCurrencyIndex(cursor);
		CurrentCurrencyAdapter currentCurrencyAdapter = new CurrentCurrencyAdapter(getActivity(), cursor);
		spinner.setAdapter(currentCurrencyAdapter);
		spinner.setSelection(position);
		listView.addHeaderView(headerView);
		final ExchangeRatesAdapter adapter = new ExchangeRatesAdapter(getActivity(), cursor, position);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				cursor.moveToPosition(position);
				String currency = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));
				adapter.setCurrentCurrency(currency);
				SettingsManager.putCurrentCurrency(currency);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		listView.setAdapter(adapter);
	}

	private int getCurrencyCurrencyIndex(Cursor cursor) {

		String code = SettingsManager.getCurrentCurrency();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)).equals(code)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getViewId() {

		return R.layout.f_currency_rates;
	}

	@Override
	public int getTitleResource() {

		return R.string.settingsExchangeRates;
	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		loaderIds.add(LoaderHelper.CURRENCIES);
	}

}
