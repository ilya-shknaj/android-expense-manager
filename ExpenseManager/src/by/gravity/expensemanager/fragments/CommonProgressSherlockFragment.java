package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import by.gravity.expensemanager.activity.DrawerActivity;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper.LoaderStatus;

public abstract class CommonProgressSherlockFragment extends SherlockProgressFragment implements LoaderCallbacks<Cursor> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(getViewId(), container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		startLoaders();
	}

	@Override
	public void onResume() {

		super.onResume();
		getSherlockActivity().getSupportActionBar().setTitle(getTitleResource());
	}

	protected boolean isLoaderFinished(String name, int id) {
		return getLoaderManager().getLoader(id) != null
				&& getLoaderManager().getLoader(id).isStarted()
				&& LoaderHelper.getIntance().getLoaderStatus(name, id) == LoaderStatus.FINISHED;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		LoaderHelper.getIntance().putLoaderStatus(getClass().getSimpleName(), loader.getId(), LoaderStatus.FINISHED);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	public void startLoaders() {

		List<Integer> loaderIds = new ArrayList<Integer>();
		getLoaderIds(loaderIds);
		for (int i = 0; i < loaderIds.size(); i++) {
			LoaderHelper.getIntance().startLoader(this, loaderIds.get(i), this);
		}
	}

	public void setDrawerLock() {

		((DrawerActivity) getActivity()).enableLock();
	}

	public abstract int getViewId();

	public abstract int getTitleResource();

	public abstract void getLoaderIds(List<Integer> loaderIds);

}
