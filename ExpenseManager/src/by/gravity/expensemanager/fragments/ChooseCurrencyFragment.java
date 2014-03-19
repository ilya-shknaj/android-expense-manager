package by.gravity.expensemanager.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;

public class ChooseCurrencyFragment extends CommonProgressSherlockFragment {

	private static final String TAG = ChooseCurrencyFragment.class.getSimpleName();

	private LayoutInflater inflater;

	public static ChooseCurrencyFragment newInstance() {
		return new ChooseCurrencyFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inflater = LayoutInflater.from(getActivity());
		startLoader();
		initBottomTabBar();
	}

	private void initBottomTabBar() {
		View saveButton = getView().findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				final RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
				for (int i = 0; i < radioGroup.getChildCount(); i++) {
					if (((RadioButton) radioGroup.getChildAt(i)).isChecked()) {
						SettingsManager.putDefaultCurrency((String) radioGroup.getChildAt(i).getTag());
						break;
					}
				}

				if (getActivity().getCallingActivity() != null) {
					getActivity().setResult(Activity.RESULT_OK);
					getActivity().finish();
				} else {
					getActivity().getSupportFragmentManager().popBackStack();
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

	private void startLoader() {
		LoaderHelper.getIntance().startLoader(this, LoaderHelper.ADD_PAYMENT_CURRENCIES_ID, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		if (id == LoaderHelper.ADD_PAYMENT_CURRENCIES_ID) {
			return new CurrencyLoader(getActivity());
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
		final RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
		String selectedCurrency = SettingsManager.getDefaultCurrency();

		radioGroup.removeAllViews();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.i_radio_button, null);
			String currency = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE));
			radioButton.setText(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)) + " (" + currency + ")");
			radioButton.setTag(currency);
			radioGroup.addView(radioButton, i);
		}

		int count = radioGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			View radioButton = radioGroup.getChildAt(i);
			if (radioButton.getTag().equals(selectedCurrency)) {
				((RadioButton) radioButton).setChecked(true);
				return;
			}
		}

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
