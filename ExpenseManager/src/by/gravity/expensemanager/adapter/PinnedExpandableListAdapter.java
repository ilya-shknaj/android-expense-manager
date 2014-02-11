package by.gravity.expensemanager.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.GroupPriceModel;
import by.gravity.expensemanager.model.PriceModel;
import by.gravity.expensemanager.view.PinnedHeaderExpListView;

public class PinnedExpandableListAdapter extends BaseExpandableListAdapter implements OnScrollListener {

	private List<GroupPriceModel> groupPriceList;
	private Context context;

	public PinnedExpandableListAdapter(Activity context, List<GroupPriceModel> groupPriceList) {
		this.groupPriceList = groupPriceList;
		this.context = context;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groupPriceList.get(groupPosition).getPriceList().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return groupPriceList.get(groupPosition).getPriceList().size();
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final PriceModel priceModel = (PriceModel) getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.i_expanded, null);
		}

		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(priceModel.getDate());

		TextView price = (TextView) convertView.findViewById(R.id.price);
		price.setText(priceModel.getPrice());

		LinearLayout categoryLayout = (LinearLayout) convertView.findViewById(R.id.categoryLayout);
		categoryLayout.removeAllViews();
		for (int i = 0; i < priceModel.getCategory().length; i++) {
			TextView category = new TextView(context);
			category.setText(priceModel.getCategory()[i]);
			categoryLayout.addView(category, i);
		}

		return convertView;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.i_collapsed, null);
		}

		ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
		if (isExpanded) {
			indicator.setBackgroundResource(R.drawable.ic_action_expand);
		} else {
			indicator.setBackgroundResource(R.drawable.ic_action_collapse);
		}

		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(groupPriceModel.getGroupName());

		TextView price = (TextView) convertView.findViewById(R.id.price);
		price.setText(groupPriceModel.getGroupPrice());

		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	/**
	 * размытие/пропадание хэдера
	 */
	public void configurePinnedHeader(View convertView, boolean isExpanded, int position) {
		getGroupView(position, isExpanded, convertView, null);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderExpListView) {
			((PinnedHeaderExpListView) view).configureHeaderView(firstVisibleItem);
		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}
