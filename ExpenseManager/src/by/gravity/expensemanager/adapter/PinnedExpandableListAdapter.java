package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ResourceCursorTreeAdapter;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.model.CollapsedModel;
import by.gravity.expensemanager.model.ExpenseModel;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.view.PinnedHeaderExpListView;

public class PinnedExpandableListAdapter extends ResourceCursorTreeAdapter implements OnScrollListener {

	public PinnedExpandableListAdapter(Context context, Cursor cursor, int groupLayout, int childLayout) {
		super(context, cursor, groupLayout, childLayout);
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

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

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
		final ExpenseModel expenseModel = getExpenseModel(cursor);

		TextView date = (TextView) view.findViewById(R.id.time);
		date.setText(expenseModel.getTime());

		TextView amount = (TextView) view.findViewById(R.id.price);
		amount.setText(expenseModel.getAmount() + Constants.SPACE_STRING + expenseModel.getCurrency());

		LinearLayout categoryLayout = (LinearLayout) view.findViewById(R.id.categoryLayout);
		categoryLayout.removeAllViews();
		for (int i = 0; i < expenseModel.getCategories().size(); i++) {
			TextView category = new TextView(context);
			category.setText(expenseModel.getCategories().get(i));
			categoryLayout.addView(category, i);
		}

	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
		CollapsedModel collapsedModel = getCollapsedModel(cursor);

		ImageView indicator = (ImageView) view.findViewById(R.id.indicator);
		if (isExpanded) {
			indicator.setBackgroundResource(R.drawable.ic_action_expand);
		} else {
			indicator.setBackgroundResource(R.drawable.ic_action_collapse);
		}

		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(collapsedModel.getDate());

		TextView price = (TextView) view.findViewById(R.id.price);
		price.setText(collapsedModel.getAmount() + Constants.SPACE_STRING + collapsedModel.getCurrency());

	}

	private CollapsedModel getCollapsedModel(Cursor cursor) {
		CollapsedModel collapsedModel = new CollapsedModel();
		collapsedModel.setAmount(String.valueOf(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_SUM_AMOUNT))));
		collapsedModel.setDate(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_DATE)));
		collapsedModel.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_SYMBOL)));
		return collapsedModel;
	}

	private ExpenseModel getExpenseModel(Cursor cursor) {
		ExpenseModel expenseModel = new ExpenseModel();
		expenseModel.setId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID)));
		expenseModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)));
		expenseModel.setCurrency(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
		expenseModel.setNote(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NOTE)));
		expenseModel.setTime(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_TIME)));
		Cursor expenseCategoryCursor = SQLDataManager.getInstance().getExpenseCategories(expenseModel.getId());
		if (expenseCategoryCursor != null && expenseCategoryCursor.getCount() > 0) {
			for (int i = 0; i < expenseCategoryCursor.getCount(); i++) {
				expenseCategoryCursor.moveToPosition(i);
				expenseModel.getCategories().add(expenseCategoryCursor.getString(expenseCategoryCursor.getColumnIndex(SQLConstants.FIELD_NAME)));
			}
			expenseCategoryCursor.close();
		}
		return expenseModel;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		Long date = groupCursor.getLong(groupCursor.getColumnIndex(SQLConstants.FIELD_DATE));
		if (date != null) {
			return SQLDataManager.getInstance().getDayExpense(date);
		}
		return null;
	}

}
