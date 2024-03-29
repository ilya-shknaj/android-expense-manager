package by.gravity.expensemanager.fragments;

import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.ExpandableListAdapter;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.loaders.ChildGroupedByCategoryNameLoader;
import by.gravity.expensemanager.fragments.loaders.ChildGroupedByDateCursorLoader;
import by.gravity.expensemanager.fragments.loaders.GroupedByCategoryNameCursorLoader;
import by.gravity.expensemanager.fragments.loaders.GroupedByDateCursorLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;

import com.actionbarsherlock.view.MenuItem;

public class OutcomeFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

	public static final String TAG = OutcomeFragment.class.getSimpleName();

	private ExpandableListAdapter adapter;

	private static final String ARG_IS_GROUPED_BY_DATE = "ARG_IS_GROUPED_BY_DATE";

	public static OutcomeFragment newInstance(boolean isGroupedByDate) {

		OutcomeFragment fragment = new OutcomeFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(ARG_IS_GROUPED_BY_DATE, isGroupedByDate);
		fragment.setArguments(bundle);

		return fragment;

	}

	@Override
	public void onResume() {

		super.onResume();
		if (!getCurrentPeriod().equals(SettingsManager.getFriendlyCurrentPeriod())) {
			setContentShown(false);
			initPeriod();
			startLoaders();
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		initPeriod();
		initListView();
		initBottomTabBar();
	}

	private void initPeriod() {

		TextView period = (TextView) getView().findViewById(R.id.period);
		period.setText(SettingsManager.getFriendlyCurrentPeriod());
		View periodLayout = getView().findViewById(R.id.periodLayout);
		periodLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showChoosePeriodFragment();
			}
		});
	}

	private String getCurrentPeriod() {

		TextView period = (TextView) getView().findViewById(R.id.period);
		return period.getText().toString();
	}

	private void initListView() {

		ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
		expandableListView.setChildDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDividerHeight(1);
		expandableListView.setGroupIndicator(null);
		if (adapter == null) {
			adapter = new ExpandableListAdapter(OutcomeFragment.this, R.layout.i_collapsed, R.layout.i_expanded, isGroupedByDate());
		}

		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				((MainActivity) getActivity()).showAddPaymentFragment(id);
				return false;
			}
		});

		// View pinnedHeaderView =
		// LayoutInflater.from(getActivity()).inflate(R.layout.i_collapsed,
		// (ViewGroup) getView().findViewById(R.id.root), false);
		// expandableListView.setPinnedHeaderView(pinnedHeaderView);
		// expandableListView.setOnScrollListener((OnScrollListener)
		// adapter);

		expandableListView.setAdapter(adapter);

	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		loaderIds.add(getGroupCursorId());

	}

	private int getGroupCursorId() {

		return isGroupedByDate() ? LoaderHelper.OUTCOME_GROUP_BY_DATE_ID : LoaderHelper.OUTCOME_GROUP_BY_CATEGORY_NAME_ID;
	}

	private boolean isGroupedByDate() {

		return getArguments().getBoolean(ARG_IS_GROUPED_BY_DATE);
	}

	private void initBottomTabBar() {

		View addExpenseButton = getView().findViewById(R.id.addButton);
		addExpenseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showAddPaymentFragment();
			}
		});

		View emptyAddExpenseButton = getView().findViewById(R.id.addExpenseButton);
		emptyAddExpenseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showAddPaymentFragment();

			}
		});

		View categoryButton = getView().findViewById(R.id.categoryButton);
		categoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainActivity) getActivity()).showOutcomeFragment(!isGroupedByDate());

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public int getViewId() {

		return R.layout.f_outcome;
	}

	@Override
	public int getTitleResource() {

		return R.string.outcome;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.OUTCOME_GROUP_BY_DATE_ID) {
			return new GroupedByDateCursorLoader(getActivity());
		} else if (id == LoaderHelper.OUTCOME_GROUP_BY_CATEGORY_NAME_ID) {
			return new GroupedByCategoryNameCursorLoader(getActivity());
		} else if (id > -1 && bundle != null) {
			final Long expenseDate = bundle.getLong(LoaderHelper.ARG_EXPENSE_DATA);
			if (expenseDate != 0) {
				return new ChildGroupedByDateCursorLoader(getActivity(), expenseDate);
			} else {
				long categoryId = bundle.getLong(LoaderHelper.ARG_CATEGORY_ID);
				return new ChildGroupedByCategoryNameLoader(getActivity(), categoryId);
			}
		}

		return null;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		int id = loader.getId();
		if (id == LoaderHelper.OUTCOME_GROUP_BY_CATEGORY_NAME_ID || id == LoaderHelper.OUTCOME_GROUP_BY_DATE_ID) {
			adapter.setGroupCursor(cursor);
			if (cursor.getCount() > 0) {
				setContentShown(true);
			} else {
				setContentEmpty(true);
			}

		} else {
			adapter.setChildrenCursor(adapter.getGroupPositionMap().get(id), cursor);
		}

	}

	private HashMap<Long, Integer> getGroupCategoryMap() {

		return adapter.getGroupCategoryMap();
	}

	public void notifyDataSetChanges(long id) {

		Loader<Object> loader = getLoaderManager().getLoader(getGroupCursorId());
		if (loader != null) {
			loader.onContentChanged();
		}

		Integer cursorId = getGroupCategoryMap().get(id);
		if (cursorId != null) {
			loader = getLoaderManager().getLoader(cursorId);
			if (loader != null) {
				loader.onContentChanged();
			}
		}

	}

	@Override
	public void setContentShown(boolean shown) {

		super.setContentShown(shown);
		View selectPeriodLayout = getView().findViewById(R.id.selectPeriodLayout);
		if (shown) {
			selectPeriodLayout.setVisibility(View.VISIBLE);
		} else {
			selectPeriodLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void setContentEmpty(boolean isEmpty) {

		super.setContentEmpty(isEmpty);
		View selectPeriodLayout = getView().findViewById(R.id.selectPeriodLayout);
		selectPeriodLayout.setVisibility(View.VISIBLE);
	}

}
