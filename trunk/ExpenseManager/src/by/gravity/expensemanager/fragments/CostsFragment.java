package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.CostsAdapter;
import by.gravity.expensemanager.model.Model;
import by.gravity.expensemanager.view.expandingcells.ExpandingListView;

import com.actionbarsherlock.view.MenuItem;

public class CostsFragment extends CommonSherlockFragment {

	private ExpandingListView expandingListView;

	public static CostsFragment newInstance() {
		CostsFragment fragment = new CostsFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		expandingListView = (ExpandingListView) getView().findViewById(R.id.expandingListView);

		List<Model> modelList = new ArrayList<Model>();
		modelList.add(new Model("25","25"));
		modelList.add(new Model("26","26"));
		modelList.add(new Model("27","27"));
		modelList.add(new Model("28","28"));
		modelList.add(new Model("29","29"));
		modelList.add(new Model("30","30"));
		modelList.add(new Model("31","31"));
		modelList.add(new Model("32","32"));
		modelList.add(new Model("33","33"));
		modelList.add(new Model("34","34"));
		modelList.add(new Model("35","35"));
		modelList.add(new Model("36","36"));
		
//		int CELL_DEFAULT_HEIGHT = 300;
//	    int NUM_OF_CELLS = 30;
//		
//		ExpandableListItem[] values = new ExpandableListItem[] {
//                new ExpandableListItem("Chameleon", R.drawable.chameleon, CELL_DEFAULT_HEIGHT,
//                        getResources().getString(R.string.short_lorem_ipsum)),
//                new ExpandableListItem("Rock", R.drawable.rock, CELL_DEFAULT_HEIGHT,
//                        getResources().getString(R.string.medium_lorem_ipsum)),
//                new ExpandableListItem("Flower", R.drawable.flower, CELL_DEFAULT_HEIGHT,
//                        getResources().getString(R.string.long_lorem_ipsum)),
//        };
//
//        List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();
//
//        for (int i = 0; i < NUM_OF_CELLS; i++) {
//            ExpandableListItem obj = values[i % values.length];
//            mData.add(new ExpandableListItem(obj.getTitle(), obj.getImgResource(),
//                    obj.getCollapsedHeight(), obj.getText()));
//        }
//
//        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_view_item, mData);

		CostsAdapter adapter = new CostsAdapter(getActivity(), R.layout.costs_full_list_item, modelList);

		expandingListView.setAdapter(adapter);
		expandingListView.setDivider(null);

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
