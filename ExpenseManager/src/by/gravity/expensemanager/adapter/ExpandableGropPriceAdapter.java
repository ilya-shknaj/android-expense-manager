package by.gravity.expensemanager.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.GroupPriceModel;
import by.gravity.expensemanager.model.PriceModel;

public class ExpandableGropPriceAdapter extends BaseExpandableListAdapter {

	private List<GroupPriceModel> groupPriceList;
	private LayoutInflater inflater;

	public ExpandableGropPriceAdapter(Activity context, List<GroupPriceModel> groupPriceList) {
		this.groupPriceList = groupPriceList;
		inflater = context.getLayoutInflater();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groupPriceList.get(groupPosition).getPriceList().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final PriceModel priceModel = (PriceModel) getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.i_expanded, null);
		}

		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(priceModel.getDate());

		TextView price = (TextView) convertView.findViewById(R.id.price);
		price.setText(priceModel.getPrice());

		LinearLayout categoryLayout = (LinearLayout) convertView.findViewById(R.id.categoryLayout);
		categoryLayout.removeAllViews();
		for (int i = 0; i < priceModel.getCategory().length; i++) {
			TextView category = (TextView) inflater.inflate(R.layout.i_category, null);
			category.setText(priceModel.getCategory()[i]);
			categoryLayout.addView(category, i);
		}

		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return groupPriceList.get(groupPosition).getPriceList().size();
	}

	public Object getGroup(int groupPosition) {
		return groupPriceList.get(groupPosition);
	}

	public String getGroupName(int groupPosition) {
		return groupPriceList.get(groupPosition).getGroupName();
	}

	public int getGroupCount() {
		return groupPriceList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupPriceModel groupPriceModel = (GroupPriceModel) getGroup(groupPosition);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.i_collapsed, null);
		}
		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(groupPriceModel.getGroupName());

		TextView price = (TextView) convertView.findViewById(R.id.price);
		price.setText(groupPriceModel.getGroupPrice());

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}