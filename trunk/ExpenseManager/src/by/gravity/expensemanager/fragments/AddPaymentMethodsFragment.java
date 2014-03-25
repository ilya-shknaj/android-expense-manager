package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import by.gravity.common.task.OnLoadCompleteListener;
import by.gravity.common.utils.GlobalUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.NumberDialog.OnInputCompleteListener;
import by.gravity.expensemanager.fragments.loaders.CurrencyLoader;
import by.gravity.expensemanager.fragments.loaders.DeletePaymentMethodLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.PaymentMethodByIdLoader;
import by.gravity.expensemanager.model.PaymentMethodModel;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.OnPositiveButtonClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class AddPaymentMethodsFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

	private static final String TAG = AddPaymentMethodsFragment.class.getSimpleName();

	private static final String ARG_PAYMENT_METHOD_ID = "ARG_PAYMENT_METHOD_ID";

	private PaymentMethodModel paymentMethodModel = null;

	public static AddPaymentMethodsFragment newInstance(Long paymentMethodId) {

		AddPaymentMethodsFragment fragment = new AddPaymentMethodsFragment();
		Bundle bundle = new Bundle();
		if (paymentMethodId != null) {
			bundle.putLong(ARG_PAYMENT_METHOD_ID, paymentMethodId);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	private Long getPaymentMethodId() {

		return getArguments().getLong(ARG_PAYMENT_METHOD_ID);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		startLoaders();
		initBottomTabBar();
	}

	@Override
	public void onResume() {

		String usedCurrencies = getUsedCurrencies();
		if (!StringUtil.isEmpty(usedCurrencies) && !usedCurrencies.equals(SettingsManager.getUsedCurrencies())) {
			LoaderHelper.getIntance().startLoader(this, LoaderHelper.ADD_PAYMENT_METHOD_CURENCIES_ID, this);

		}
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.add_payment, menu);
		menu.findItem(R.id.remove).setVisible(getPaymentMethodId() != 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals(getString(R.string.remove))) {
			DialogHelper.showConfirmDialog(getActivity(), R.string.remove, R.string.removePayemntMethodMessage, new OnPositiveButtonClickListener() {

				@Override
				public void onPositiveButtonClicked() {

					setContentShown(false);
					LoaderHelper.getIntance().startLoader(AddPaymentMethodsFragment.this, LoaderHelper.DELETE_PAYMENT_METHOD_ID,
							AddPaymentMethodsFragment.this);
				}
			});
		}
		return false;
	}

	private void startLoaders() {

		LoaderHelper.getIntance().startLoader(this, LoaderHelper.ADD_PAYMENT_METHOD_CURENCIES_ID, this);
		if (getPaymentMethodId() != 0) {
			LoaderHelper.getIntance().startLoader(this, LoaderHelper.PAYMENT_METHOD_BY_ID, this);
		}
	}

	private void initCurrency(List<String> currencyList) {

		final Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencyList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}

	private void initBottomTabBar() {

		final View cancelButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GlobalUtil.hideSoftKeyboard(getActivity());
				getFragmentManager().popBackStack();
			}
		});

		final View saveButton = getView().findViewById(R.id.tabBarLayout).findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText name = (EditText) getView().findViewById(R.id.name);
				if (name.getText().toString().length() == 0) {
					Toast.makeText(getActivity(), R.string.emptyName, Toast.LENGTH_SHORT).show();
					name.requestFocus();
				}

				EditText balance = (EditText) getView().findViewById(R.id.balance);

				Spinner spinner = (Spinner) getView().findViewById(R.id.currency);

				EditText note = (EditText) getView().findViewById(R.id.note);

				if (SettingsManager.getPaymentMethod().equals(getString(R.string.emptyPaymentMethods))) {
					SettingsManager.putPaymentMethod(name.getText().toString());
				}

				if (getPaymentMethodId() == 0) {
					SQLDataManager.getInstance().addPaymentMethod(name.getText().toString(), note.getText().toString(), balance.getText().toString(),
							spinner.getSelectedItem().toString(), new OnLoadCompleteListener<Void>() {

								@Override
								public void onComplete(Void result) {

									GlobalUtil.hideSoftKeyboard(getActivity());
									getFragmentManager().popBackStack();
								}
							});
				} else {
					SQLDataManager.getInstance().updatePaymentMethod(getPaymentMethodId(), name.getText().toString(), note.getText().toString(),
							balance.getText().toString(), spinner.getSelectedItem().toString(), new OnLoadCompleteListener<Void>() {

								@Override
								public void onComplete(Void result) {

									GlobalUtil.hideSoftKeyboard(getActivity());
									getFragmentManager().popBackStack();
								}
							});
				}
			}

		});
	}

	private String getUsedCurrencies() {

		if (getView() != null) {
			Spinner spinner = (Spinner) getView().findViewById(R.id.currency);
			List<String> currencies = new ArrayList<String>();
			if (spinner != null && spinner.getAdapter() != null) {
				for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
					currencies.add((String) spinner.getAdapter().getItem(i));
				}
			}
			return StringUtil.strJoin(currencies.toArray(new String[] {}), ",");
		}

		return null;
	}

	private void initView() {

		final EditText balance = (EditText) getView().findViewById(R.id.balance);
		balance.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean focused) {

				if (focused) {
					balance.clearFocus();
					showNumberDialog(balance.getText().toString());
				}
			}
		});

		if (paymentMethodModel != null) {
			balance.setText(StringUtil.convertNumberToHumanFriednly(paymentMethodModel.getBalance()));

			final EditText name = (EditText) getView().findViewById(R.id.name);
			name.setText(paymentMethodModel.getName());

			final EditText note = (EditText) getView().findViewById(R.id.note);
			note.setText(paymentMethodModel.getNote());

			final Spinner currency = (Spinner) getView().findViewById(R.id.currency);
			final int count = currency.getCount();

			for (int i = 0; i < count; i++) {
				if (currency.getItemAtPosition(i).toString().equals(paymentMethodModel.getCurrency())) {
					currency.setSelection(i);
				}
			}

		}

	}

	private void showNumberDialog(String costs) {

		NumberDialog numberDialog = NumberDialog.newInstance(costs, new OnInputCompleteListener() {

			@Override
			public void onInputCompleted(String value) {

				EditText costEditText = (EditText) getView().findViewById(R.id.balance);
				costEditText.setText(value);
			}
		});
		numberDialog.show(getFragmentManager(), Number.class.getSimpleName());
	}

	@Override
	public int getViewId() {

		return R.layout.f_add_payment_method;
	}

	@Override
	public int getTitleResource() {

		return R.string.addPaymentMethod;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle budle) {

		if (id == LoaderHelper.ADD_PAYMENT_METHOD_CURENCIES_ID) {
			return new CurrencyLoader(getActivity(), true);
		} else if (id == LoaderHelper.PAYMENT_METHOD_BY_ID) {
			return new PaymentMethodByIdLoader(getActivity(), getPaymentMethodId());
		} else if (id == LoaderHelper.DELETE_PAYMENT_METHOD_ID) {
			return new DeletePaymentMethodLoader(getActivity(), getPaymentMethodId());
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.ADD_PAYMENT_METHOD_CURENCIES_ID) {
			List<String> currencyList = parseCurrency(cursor);
			initCurrency(currencyList);
		} else if (loader.getId() == LoaderHelper.PAYMENT_METHOD_BY_ID) {
			parsePaymentMethod(cursor);
		} else if (loader.getId() == LoaderHelper.DELETE_PAYMENT_METHOD_ID) {
			((MainActivity) getActivity()).delayedPopBackStack();
		}

		if (isLoaderFinished(TAG, LoaderHelper.ADD_PAYMENT_METHOD_CURENCIES_ID)
				&& (getPaymentMethodId() == 0 ? true : isLoaderFinished(TAG, LoaderHelper.PAYMENT_METHOD_BY_ID))) {
			initView();
			setContentShown(true);
		}

	}

	private void parsePaymentMethod(Cursor cursor) {

		paymentMethodModel = new PaymentMethodModel();
		if (cursor.moveToFirst()) {
			paymentMethodModel.setBalance(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_BALANCE)));
			paymentMethodModel.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
			paymentMethodModel.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
			paymentMethodModel.setNote(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NOTE)));

			cursor.close();
		}

	}

	private List<String> parseCurrency(Cursor cursor) {

		List<String> currencyList = new ArrayList<String>();
		if (cursor != null && cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				currencyList.add(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
			}

			cursor.close();
		}
		return currencyList;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		LoaderHelper.getIntance().clearLoaderStatus(TAG);
	}

}
