package by.gravity.expensemanager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.PaymentMethodsAdapter;
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
			}
		});
	}

	private void startLoader() {
		getLoaderManager().initLoader(LoaderHelper.GET_PAYMENT_METHODS_ID, null, this);
	}

	private void initListView(Cursor cursor) {
		ListView listView = (ListView) getView().findViewById(R.id.listView);
		PaymentMethodsAdapter adapter = new PaymentMethodsAdapter(getActivity(), R.layout.i_payments_methods_detail_pay, cursor);
		listView.setAdapter(adapter);
	}

	@Override
	public int getViewId() {
		return R.layout.f_payment_methods;
	}

	@Override
	public int getTitleResource() {
		return R.string.paymentsMethods;
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
	public void onLoaderReset(Loader<Cursor> loader) {

	}

}
