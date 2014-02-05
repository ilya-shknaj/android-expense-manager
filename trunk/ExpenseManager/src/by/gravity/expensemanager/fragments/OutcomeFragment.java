package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.ExpandableGropPriceAdapter;
import by.gravity.expensemanager.model.GroupPriceModel;
import by.gravity.expensemanager.model.PriceModel;

import com.actionbarsherlock.view.MenuItem;

public class OutcomeFragment extends CommonSherlockFragment {

	public static OutcomeFragment newInstance() {
		OutcomeFragment fragment = new OutcomeFragment();

		return fragment;
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
	}

	private void initListView() {
		ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
		expandableListView.setChildDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDivider(getResources().getDrawable(R.color.divider));
		expandableListView.setDividerHeight(1);
		ExpandableGropPriceAdapter adapter = new ExpandableGropPriceAdapter(getActivity(), getGroupPriceList());
		expandableListView.setAdapter(adapter);
	}

	private List<GroupPriceModel> getGroupPriceList() {
		List<GroupPriceModel> groupPriceModels = new ArrayList<GroupPriceModel>();
		for (int i = 0; i < 10; i++) {
			GroupPriceModel groupPriceModel = new GroupPriceModel();
			groupPriceModel.setGroupName(i + " январ€");
			groupPriceModel.setGroupPrice("" + 10 + i + "$");
			for (int j = 0; j < 10; j++) {
				PriceModel priceModel = new PriceModel();
				priceModel.setDate(j + ".01.2014");
				priceModel.setPrice(j + "$");
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
		return R.layout.f_costs;
	}

}
