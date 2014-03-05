package by.gravity.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonProgressSherlockFragment extends SherlockProgressFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getViewId(), container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setTitle(getTitleResource());
	}

	public abstract int getViewId();

	public abstract int getTitleResource();

}
