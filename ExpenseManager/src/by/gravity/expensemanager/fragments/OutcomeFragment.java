package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.adapter.PinnedExpandableListAdapter;
import by.gravity.expensemanager.model.GroupPriceModel;
import by.gravity.expensemanager.model.PriceModel;
import by.gravity.expensemanager.view.PinnedHeaderExpListView;

import com.actionbarsherlock.view.MenuItem;

public class OutcomeFragment extends CommonSherlockFragment {

	public static OutcomeFragment newInstance() {
		return new OutcomeFragment();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPeriod();
		initListView();
	}

	private void initPeriod() {
		TextView period = (TextView) getView().findViewById(R.id.period);
		period.setText("01 январ€ - 01 ‘еврал€");
		View periodLayout = getView().findViewById(R.id.periodLayout);
		periodLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showChoosePeriodFragment();
			}
		});
	}

	private void initListView() {
		PinnedHeaderExpListView expandableListView = (PinnedHeaderExpListView) getView().findViewById(R.id.expandableListView);
		expandableListView.setChildDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDividerHeight(1);
		expandableListView.setGroupIndicator(null);
		PinnedExpandableListAdapter adapter = new PinnedExpandableListAdapter(getActivity(), getGroupPriceList());

		expandableListView.setAdapter(adapter);

		View pinnedHeaderView = LayoutInflater.from(getActivity())
				.inflate(R.layout.i_collapsed, (ViewGroup) getView().findViewById(R.id.root), false);
		expandableListView.setPinnedHeaderView(pinnedHeaderView);
		expandableListView.setOnScrollListener((OnScrollListener) adapter);

		expandableListView.setAdapter(adapter);
	}

	private List<GroupPriceModel> getGroupPriceList() {
		List<GroupPriceModel> groupPriceModels = new ArrayList<GroupPriceModel>();
		for (int i = 0; i < 10; i++) {
			GroupPriceModel groupPriceModel = new GroupPriceModel();
			groupPriceModel.setGroupName(i + " январ€");
			groupPriceModel.setGroupPrice("" + 10000 + i + " $");
			for (int j = 0; j < 10; j++) {
				PriceModel priceModel = new PriceModel();
				priceModel.setDate(j + ".01.2014");
				priceModel.setPrice(j + "$");
				priceModel.setCategory(new String[] { "ѕродукты" });
				groupPriceModel.getPriceList().add(priceModel);
			}
			groupPriceModels.add(groupPriceModel);
		}

		return groupPriceModels;
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
