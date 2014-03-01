package by.gravity.expensemanager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.ExpandableListAdapter;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;

import com.actionbarsherlock.view.MenuItem;

public class OutcomeFragment extends CommonSherlockFragment {

	private boolean isGroupedByDate = true;

	private ExpandableListAdapter adapter;

	public static OutcomeFragment newInstance() {
		return new OutcomeFragment();

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

	private void initListView() {
		ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
		expandableListView.setChildDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDividerHeight(1);
		expandableListView.setGroupIndicator(null);
		Cursor cursor = isGroupedByDate ? SQLDataManager.getInstance().getGroupedByDateCursor() : SQLDataManager.getInstance()
				.getGroupedByCategoryNameCursor();
		adapter = new ExpandableListAdapter(getActivity(), cursor, R.layout.i_collapsed, R.layout.i_expanded, isGroupedByDate);

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
		// expandableListView.setOnScrollListener((OnScrollListener) adapter);

		expandableListView.setAdapter(adapter);

	}

	private void initBottomTabBar() {
		View addExpenseButton = getView().findViewById(R.id.addButton);
		addExpenseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showAddPaymentFragment();
			}
		});

		View categoryButton = getView().findViewById(R.id.categoryButton);
		categoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isGroupedByDate = !isGroupedByDate;
				if (isGroupedByDate) {
					adapter.changeCursor(SQLDataManager.getInstance().getGroupedByDateCursor(), isGroupedByDate);
				} else {
					adapter.changeCursor(SQLDataManager.getInstance().getGroupedByCategoryNameCursor(), isGroupedByDate);
				}

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

}
