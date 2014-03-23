package by.gravity.expensemanager.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.ChooseCurrencyAdapter;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;

public class ChooseCurrencyFragment extends CommonProgressSherlockFragment {

	private static final String TAG = ChooseCurrencyFragment.class.getSimpleName();

	private static final String ARG_SHOW_ONLY_SHORT_CURRENCIES = "ARG_SHOW_ONLY_SHORT_CURRENCIES";

	public static ChooseCurrencyFragment newInstance(boolean showOnlyShortCurrencies) {

		ChooseCurrencyFragment fragment = new ChooseCurrencyFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(ARG_SHOW_ONLY_SHORT_CURRENCIES, showOnlyShortCurrencies);
		fragment.setArguments(bundle);
		return fragment;
	}

	private boolean getShowOnlyShortCurrencies() {

		return getArguments().getBoolean(ARG_SHOW_ONLY_SHORT_CURRENCIES);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		startLoader();
		initBottomTabBar();
	}

	private void initBottomTabBar() {

		View saveButton = getView().findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				ListView listView = (ListView) getView().findViewById(R.id.listView);
				listView.getCheckedItemIds();
			}
		});

		View cancelButton = getView().findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (getActivity().getCallingActivity() != null) {
					getActivity().setResult(Activity.RESULT_CANCELED);
					getActivity().finish();
				} else {
					getActivity().getSupportFragmentManager().popBackStack();
				}

			}
		});
	}

	private void startLoader() {

		LoaderHelper.getIntance().startLoader(this, LoaderHelper.ADD_PAYMENT_CURRENCIES_ID, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) {
			return new CurrencyLoader(getActivity(), getShowOnlyShortCurrencies());
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) {
			initCurrencies(cursor);
		}

		if (isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_CURRENCIES_ID)) {
			setContentShown(true);
		}
	}

	private void initCurrencies(Cursor cursor) {

		ListView listView = (ListView) getView().findViewById(R.id.listView);
		ChooseCurrencyAdapter adapter = new ChooseCurrencyAdapter(getActivity(), cursor);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setItemsCanFocus(false);

	}

	@Override
	public int getViewId() {

		return R.layout.f_choose_currency;
	}

	@Override
	public int getTitleResource() {

		return R.string.settingDefaultCurrency;
	}

}
