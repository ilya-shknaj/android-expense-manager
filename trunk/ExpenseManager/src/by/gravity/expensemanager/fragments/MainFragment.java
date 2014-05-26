package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper.LoaderStatus;
import by.gravity.expensemanager.fragments.loaders.PaymentMethodsLoader;
import by.gravity.expensemanager.fragments.loaders.RefreshRatesLoader;
import by.gravity.expensemanager.fragments.loaders.SumBalanceLoader;
import by.gravity.expensemanager.model.PaymentMethodModel;
import by.gravity.expensemanager.util.Constants;

public class MainFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

	public static final String TAG = MainFragment.class.getSimpleName();

	public static MainFragment newInstance() {

		return new MainFragment();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		initBottomBar();

	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		loaderIds.add(LoaderHelper.GET_PAYMENT_METHODS_ID);

		if (SettingsManager.getUpdateCurrencyPeriod().equals(getString(R.string.settingsPeriodEveryDay))
				&& TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - SettingsManager.getExchangeRatesLastUpdateTime()) >= 24
				&& LoaderHelper.getIntance().getLoaderStatus(TAG, LoaderHelper.REFRESH_CURRENCY_RATE_ID) != LoaderStatus.STARTED) {
			loaderIds.add(LoaderHelper.REFRESH_CURRENCY_RATE_ID);
		}

	}

	private void initPaymentsMethods(List<PaymentMethodModel> paymentMethods) {

		LinearLayout paymentsMethodLayout = (LinearLayout) getView().findViewById(R.id.paymentsMethodLayout);

		paymentsMethodLayout.removeAllViews();

		for (int i = 0; i < paymentMethods.size(); i++) {
			RelativeLayout paymentDetailLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.i_payments_methods_detail_pay,
					null);
			TextView name = (TextView) paymentDetailLayout.findViewById(R.id.name);
			name.setText(paymentMethods.get(i).getName());

			TextView balance = (TextView) paymentDetailLayout.findViewById(R.id.balance);
			balance.setText(paymentMethods.get(i).getBalance() + Constants.SPACE_STRING + paymentMethods.get(i).getCurrency());
			paymentsMethodLayout.addView(paymentDetailLayout, i);
		}

		View paymentMethodCard = getView().findViewById(R.id.paymentMethodCard);
		paymentMethodCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showPaymentMethodsFragment();
			}
		});

	}

	private boolean hasPaymentMethods() {

		LinearLayout paymentsMethodLayout = (LinearLayout) getView().findViewById(R.id.paymentsMethodLayout);
		return paymentsMethodLayout.getChildCount() > 0;
	}

	private void initBalance(String balance) {

		TextView balanceTextView = (TextView) getView().findViewById(R.id.balanceLayout).findViewById(R.id.balance);
		balanceTextView.setText(StringUtil.alignToRight(balance));
	}

	private void initBottomBar() {

		View addPayment = getView().findViewById(R.id.addPayment);
		addPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((MainActivity) getActivity()).showAddPaymentFragment();
			}
		});

		View addPaymentLayout = getView().findViewById(R.id.emptyAddPaymentLayout);
		addPaymentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showAddPaymentMethodFragment();
			}
		});
	}

	@Override
	public int getViewId() {

		return R.layout.f_main;
	}

	@Override
	public int getTitleResource() {

		return R.string.main;
	}

	private List<PaymentMethodModel> parsePaymentMethods(Cursor cursor) {

		List<PaymentMethodModel> paymentMethods = new ArrayList<PaymentMethodModel>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			PaymentMethodModel paymentDetail = new PaymentMethodModel();
			paymentDetail.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
			paymentDetail.setBalance(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_BALANCE)));
			paymentDetail.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));

			paymentMethods.add(paymentDetail);
		}

		return paymentMethods;
	}

	private String parseBalance(Cursor cursor) {

		if (cursor.moveToFirst()) {

			String balance = cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_BALANCE));
			return StringUtil.convertNumberToHumanFriednly(balance);
		}
		return null;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.GET_PAYMENT_METHODS_ID) {
			return new PaymentMethodsLoader(getActivity());
		} else if (id == LoaderHelper.SUM_BALANCE_ID) {
			return new SumBalanceLoader(getActivity());
		} else if (id == LoaderHelper.REFRESH_CURRENCY_RATE_ID) {
			return new RefreshRatesLoader(getActivity());
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		if (loader.getId() == LoaderHelper.GET_PAYMENT_METHODS_ID) {
			List<PaymentMethodModel> paymentMethods = parsePaymentMethods(cursor);
			initPaymentsMethods(paymentMethods);
			LoaderHelper.getIntance().startLoader(this, LoaderHelper.SUM_BALANCE_ID, this);
		} else if (loader.getId() == LoaderHelper.SUM_BALANCE_ID) {
			String balance = parseBalance(cursor);
			if (!StringUtil.isEmpty(balance)) {
				initBalance(balance);
			}
		}

		if (isLoaderFinished(TAG, LoaderHelper.SUM_BALANCE_ID) && isLoaderFinished(TAG, LoaderHelper.GET_PAYMENT_METHODS_ID)) {
			if (hasPaymentMethods()) {
				setContentShown(true);
			} else {
				setContentEmpty(true);
			}
		}
	}

	public void notifyDataSetChanges() {

		Loader<Object> loader = getLoaderManager().getLoader(LoaderHelper.GET_PAYMENT_METHODS_ID);
		loader.onContentChanged();

	}

}
