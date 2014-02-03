package by.gravity.expensemanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.Model;

public class CostsAdapter extends ExpandableArrayAdapter<Model> {

	public CostsAdapter(Context context, int layoutViewResourceId, List<Model> data) {
		super(context, layoutViewResourceId, data);
	}

	@Override
	public View initView(View view, Model model) {
		LinearLayout collapsedLayout = (LinearLayout) view.findViewById(R.id.collapsed_layout);
		TextView collapsedName = (TextView) collapsedLayout.findViewById(R.id.date);
		TextView collapsedPrice = (TextView) collapsedLayout.findViewById(R.id.price);

		collapsedName.setText(model.getDate());
		collapsedPrice.setText(model.getPrice());

		LinearLayout expandedLayout = (LinearLayout) view.findViewById(R.id.expanded_layout);
		TextView name = (TextView) expandedLayout.findViewById(R.id.date);
		TextView price = (TextView) expandedLayout.findViewById(R.id.price);

		name.setText(model.getDate());
		price.setText(model.getPrice());

		return view;
	}

}
