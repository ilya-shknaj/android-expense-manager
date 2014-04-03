package by.gravity.expensemanager.fragments;

import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.CurrentCurrencyAdapter;
import by.gravity.expensemanager.adapter.ExchangeRatesAdapter;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.UpdateRateLoader;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.onEditCompleteListener;
import by.gravity.expensemanager.util.GlobalUtils;

public class ExchangeRatesFragment extends CommonProgressSherlockFragment {

	public static final String TAG = ExchangeRatesFragment.class.getSimpleName();
	
	private static final String ARG_CODE = "ARG_CODE";

	private static final String ARG_RATE = "ARG_RATE";

	private View headerView = null;

	public static ExchangeRatesFragment newInstance() {

		ExchangeRatesFragment fragment = new ExchangeRatesFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		setDrawerLock();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.CURRENCIES) {
			return new CurrencyLoader(getActivity(), false);
		} else if (id == LoaderHelper.UPDATE_CURRENCY_RATE_ID) {
			return new UpdateRateLoader(getActivity(), bundle.getString(ARG_CODE), bundle.getDouble(ARG_RATE));
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.CURRENCIES) {
			initListView(cursor);
			setContentShown(true);
		} else if (loader.getId() == LoaderHelper.UPDATE_CURRENCY_RATE_ID) {
			startLoaders();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.exchange_rates, menu);
	}

	private void initListView(final Cursor cursor) {

		ListView listView = (ListView) getView().findViewById(R.id.listView);
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.i_current_currency, null);
		Spinner spinner = (Spinner) headerView.findViewById(R.id.spinner);
		int position = getCurrencyCurrencyIndex(cursor);
		CurrentCurrencyAdapter currentCurrencyAdapter = new CurrentCurrencyAdapter(getActivity(), cursor);
		spinner.setAdapter(currentCurrencyAdapter);
		spinner.setSelection(position);
		if (this.headerView != null) {
			listView.removeHeaderView(this.headerView);
		}
		listView.addHeaderView(headerView);
		this.headerView = headerView;
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				cursor.moveToPosition(position - 1);
				final String code = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));
				if (!code.equals(adapter.getCurrentCurrency())) {
					TextView rate = (TextView) view.findViewById(R.id.rate);
					DialogHelper.showNumberEditDialog(getActivity(), String.format(getString(R.string.rate), code), 0, rate.getText().toString(),
							new onEditCompleteListener() {

								@Override
								public void onEditCompelted(double value) {

									setContentShown(false);
									Bundle bundle = new Bundle();
									bundle.putString(ARG_CODE, !code.equals(Constants.USD) ? code : adapter.getCurrentCurrency());
									bundle.putDouble(ARG_RATE, GlobalUtils.convertToUsdRate(code, adapter.getCurrencyRate(), value));
									LoaderHelper.getIntance().startLoader(ExchangeRatesFragment.this, LoaderHelper.UPDATE_CURRENCY_RATE_ID,
											ExchangeRatesFragment.this, bundle);

								}
							});
				}
			}
		});
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
