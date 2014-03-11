package by.gravity.expensemanager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.ExpandableListAdapter;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.outcome.ChildGroupedByCategoryNameLoader;
import by.gravity.expensemanager.fragments.loaders.outcome.ChildGroupedByDateCursorLoader;
import by.gravity.expensemanager.fragments.loaders.outcome.GroupedByCategoryNameCursorLoader;
import by.gravity.expensemanager.fragments.loaders.outcome.GroupedByDateCursorLoader;

import com.actionbarsherlock.view.MenuItem;

public class OutcomeFragment extends CommonProgressSherlockFragment implements LoaderCallbacks<Cursor> {

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPeriod();
		initListView();
		initBottomTabBar();
		startLoader();
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

	private void initListView() {
		ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
		expandableListView.setChildDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDividerHeight(1);
		expandableListView.setGroupIndicator(null);

		adapter = new ExpandableListAdapter(OutcomeFragment.this, R.layout.i_collapsed, R.layout.i_expanded, isGroupedByDate());

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
	
	private void startLoader(){
		int loaderId = isGroupedByDate() ? LoaderHelper.OUTCOME_GROUP_BY_DATE_ID : LoaderHelper.OUTCOME_GROUP_BY_CATEGORY_NAME_ID;

		if (getLoaderManager().getLoader(loaderId) != null && !getLoaderManager().getLoader(loaderId).isAbandoned()) {
			getLoaderManager().restartLoader(loaderId, null, this);

		} else {
			getLoaderManager().initLoader(loaderId, null, this);
		}

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
				int categoryId = bundle.getInt(LoaderHelper.ARG_CATEGORY_ID);
				return new ChildGroupedByCategoryNameLoader(getActivity(), categoryId);
			}
		}

		return null;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int id = loader.getId();
		if (id == LoaderHelper.OUTCOME_GROUP_BY_CATEGORY_NAME_ID || id == LoaderHelper.OUTCOME_GROUP_BY_DATE_ID) {
			if (cursor.getCount() > 0) {
				adapter.setGroupCursor(cursor);
				setContentShown(true);
			} else {
				setContentEmpty(true);
			}

		} else {
			adapter.setChildrenCursor(adapter.getGroupMap().get(id), cursor);
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

}
