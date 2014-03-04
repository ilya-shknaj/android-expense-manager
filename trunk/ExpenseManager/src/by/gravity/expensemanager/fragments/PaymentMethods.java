package by.gravity.expensemanager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.PaymentMethodsLoader;

public class PaymentMethods extends CommonSherlockFragment implements LoaderCallbacks<Cursor> {

	@Override
	public int getViewId() {
		// TODO Auto-generated method stub
		return 0;
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
			// TODO set adapter
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

}
