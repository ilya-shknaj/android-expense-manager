package by.gravity.expensemanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.PaymentMethodsAdapter;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.PaymentMethodsLoader;

public class PaymentMethodsFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

	public static PaymentMethodsFragment newInstance() {
		PaymentMethodsFragment fragment = new PaymentMethodsFragment();
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		startLoader();
		initBottomBar();

	}

	private void initBottomBar() {
		View addButton = getView().findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showAddPaymentMethodFragment();
			}
		});
	}

	private void initListView(Cursor cursor) {
		final ListView listView = (ListView) getView().findViewById(R.id.listView);
		final PaymentMethodsAdapter adapter = new PaymentMethodsAdapter(getActivity(), cursor);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (getActivity().getCallingActivity() == null) {
					((MainActivity) getActivity()).showAddPaymentMethodFragment(id);
				} else {
					Cursor cursor = (Cursor) adapter.getItem(position);
					Intent intent = new Intent(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
					getActivity().setResult(Activity.RESULT_OK, intent);
					getActivity().finish();
				}
			}
		});
	}

	private void startLoader() {
		if (getLoaderManager().getLoader(LoaderHelper.GET_PAYMENT_METHODS_ID) != null
				&& !getLoaderManager().getLoader(LoaderHelper.GET_PAYMENT_METHODS_ID).isAbandoned()) {
			getLoaderManager().restartLoader(LoaderHelper.GET_PAYMENT_METHODS_ID, null, this);
		} else {
			getLoaderManager().initLoader(LoaderHelper.GET_PAYMENT_METHODS_ID, null, this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		if (id == LoaderHelper.GET_PAYMENT_METHODS_ID) {
			return new PaymentMethodsLoader(getActivity());
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int id = loader.getId();
		if (id == LoaderHelper.GET_PAYMENT_METHODS_ID) {
			initListView(cursor);

			if (cursor.getCount() > 0) {
				setContentShown(true);
			} else {
				setContentEmpty(true);
			}
		}
	}

	@Override
	public int getViewId() {
		return R.layout.f_payment_methods;
	}

	@Override
	public int getTitleResource() {
		return R.string.paymentsMethods;
	}

}
