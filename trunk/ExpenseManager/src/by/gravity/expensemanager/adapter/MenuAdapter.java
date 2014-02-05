package by.gravity.expensemanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import by.gravity.common.adapter.AbstractAdapter;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.MenuModel;

public class MenuAdapter extends AbstractAdapter<MenuModel> {

	public MenuAdapter(Context c, int pItemResource, List<MenuModel> pList) {
		super(c, pItemResource, pList);

	}

	@Override
	public void init(View convertView, MenuModel item) {
		TextView textView = (TextView) convertView.findViewById(R.id.text);
		textView.setCompoundDrawablesWithIntrinsicBounds(item.getDrawableId(), 0, 0, 0);
		textView.setText(item.getText());
		
	}

}
