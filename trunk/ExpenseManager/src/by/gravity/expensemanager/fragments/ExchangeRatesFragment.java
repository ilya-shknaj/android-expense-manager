package by.gravity.expensemanager.fragments;

import java.util.List;

import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.ExchangeRatesAdapter;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.ListView;

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

	private void initListView(Cursor cursor) {

		ListView listView = (ListView) getView().findViewById(R.id.listView);
		ExchangeRatesAdapter adapter = new ExchangeRatesAdapter(getActivity(), cursor);
		listView.setAdapter(adapter);
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
