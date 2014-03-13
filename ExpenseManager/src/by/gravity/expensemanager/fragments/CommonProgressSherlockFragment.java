package by.gravity.expensemanager.fragments;

import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper.LoaderStatus;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonProgressSherlockFragment extends SherlockProgressFragment implements LoaderCallbacks<Cursor> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getViewId(), container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setTitle(getTitleResource());
	}

	protected boolean isLoaderFinished(String name, int id) {
		return getLoaderManager().getLoader(id) != null && getLoaderManager().getLoader(id).isStarted()
				&& LoaderHelper.getIntance().getLoaderStatus(name, id) == LoaderStatus.FINISHED;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		LoaderHelper.getIntance().putLoaderStatus(getClass().getSimpleName(), loader.getId(), LoaderStatus.FINISHED);
	}

	public abstract int getViewId();

	public abstract int getTitleResource();

}
