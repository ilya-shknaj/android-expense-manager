package by.gravity.expensemanager.fragments;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.ChooseCurrencyAdapter;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.UpdateUsedCurrencies;

public class ChooseCurrencyFragment extends CommonProgressSherlockFragment {

	public static final String TAG = ChooseCurrencyFragment.class.getSimpleName();

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
		initBottomTabBar();

	}

	private void initBottomTabBar() {

		View saveButton = getView().findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ListView listView = (ListView) getView().findViewById(R.id.listView);
				if (listView.getCheckedItemIds().length > 0) {
					LoaderHelper.getIntance().startLoader(ChooseCurrencyFragment.this, LoaderHelper.UPDATE_USED_CURRENCIES_ID,
							ChooseCurrencyFragment.this);
				} else {
					Toast.makeText(getActivity(), R.string.selectUsedCurrencies, Toast.LENGTH_SHORT).show();
				}
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

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		loaderIds.add(LoaderHelper.CURRENCIES);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.CURRENCIES) {
			return new CurrencyLoader(getActivity(), getShowOnlyShortCurrencies());
		} else if (id == LoaderHelper.UPDATE_USED_CURRENCIES_ID) {
			ListView listView = (ListView) getView().findViewById(R.id.listView);
			return new UpdateUsedCurrencies(getActivity(), listView.getCheckedItemIds());
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.CURRENCIES) {
			initCurrencies(cursor);
		} else if (loader.getId() == LoaderHelper.UPDATE_USED_CURRENCIES_ID) {
			getActivity().setResult(Activity.RESULT_OK);
			getActivity().finish();
		}

		if (isLoaderFinished(TAG, LoaderHelper.CURRENCIES)) {
			setContentShown(true);
		}
	}

	private void initCurrencies(Cursor cursor) {

		final ListView listView = (ListView) getView().findViewById(R.id.listView);
		ChooseCurrencyAdapter adapter = new ChooseCurrencyAdapter(getActivity(), cursor);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);

		// TODO bad solution
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (cursor.getInt(cursor.getColumnIndex(SQLConstants.FIELD_IS_USED)) == 1) {
				listView.setItemChecked(i, true);
			}
		}

	}

	@Override
	public int getViewId() {

		return R.layout.f_choose_currency;
	}

	@Override
	public int getTitleResource() {

		return R.string.settingsUsedCurrencies;
	}

}
