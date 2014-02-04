package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ExpandableListView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.ExpandableGropPriceAdapter;
import by.gravity.expensemanager.model.GroupPriceModel;
import by.gravity.expensemanager.model.PriceModel;

import com.actionbarsherlock.view.MenuItem;

public class CostsFragment extends CommonSherlockFragment {

	public static CostsFragment newInstance() {
		CostsFragment fragment = new CostsFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
		ExpandableGropPriceAdapter adapter = new ExpandableGropPriceAdapter(getActivity(), getGroupPriceList());
		expandableListView.setAdapter(adapter);
	}

	private List<GroupPriceModel> getGroupPriceList() {
		List<GroupPriceModel> groupPriceModels = new ArrayList<GroupPriceModel>();
		for (int i = 0; i < 5; i++) {
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
		return R.layout.f_main;
	}

}
