package by.gravity.expensemanager.fragments;

import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.CategoryAdapter;
import by.gravity.expensemanager.fragments.loaders.CategoriesLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;

public class CategoriesFragment extends CommonProgressSherlockFragment {

	public static final String TAG = CategoriesFragment.class.getSimpleName();

	public static CategoriesFragment newInstance() {

		return new CategoriesFragment();
	}

	private CategoriesFragment() {

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.ADD_PAYMENT_CATEGORIES_ID) {
			return new CategoriesLoader(getActivity());
		}
		return null;
	}

	@Override
	public int getViewId() {

		return R.layout.f_categories;
	}

	@Override
	public int getTitleResource() {

		return R.string.categories;
	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		loaderIds.add(LoaderHelper.ADD_PAYMENT_CATEGORIES_ID);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		super.onLoadFinished(loader, cursor);
		int id = loader.getId();
		if (id == LoaderHelper.ADD_PAYMENT_CATEGORIES_ID) {
			if (cursor.getCount() > 0) {
				initView(cursor);
				setContentShown(true);
			} else {
				setContentEmpty(true);
			}
		}

	}

	private void initView(Cursor cursor) {

		ListView listView = (ListView) getView().findViewById(R.id.listView);
		listView.setAdapter(new CategoryAdapter(getActivity(), cursor));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				((MainActivity) getActivity()).showAddCategoryFragment((int) id);
			}
		});

		View addButton = getView().findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showAddCategoryFragment(null);
			}
		});
	}

	public void notifyDataSetChanged() {

		Loader<Object> loader = getLoaderManager().getLoader(LoaderHelper.ADD_PAYMENT_CATEGORIES_ID);
		if (loader != null) {
			loader.onContentChanged();
		}
	}

}
