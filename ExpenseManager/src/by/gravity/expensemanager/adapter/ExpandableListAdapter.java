package by.gravity.expensemanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ResourceCursorTreeAdapter;
import android.widget.TextView;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.model.CollapsedModelGroupedByCategoryName;
import by.gravity.expensemanager.model.CollapsedModelGroupedByDate;
import by.gravity.expensemanager.model.ExpenseGroupedByCategoryNameModel;
import by.gravity.expensemanager.model.ExpenseGroupedByDateModel;

public class ExpandableListAdapter extends ResourceCursorTreeAdapter {

	private boolean isGroupedByDate;

	public ExpandableListAdapter(Context context, Cursor cursor, int groupLayout, int childLayout, boolean isGroupedByDate) {
		super(context, cursor, groupLayout, childLayout);
		this.isGroupedByDate = isGroupedByDate;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void configurePinnedHeader(View convertView, boolean isExpanded, int position) {
		// getGroupView(position, isExpanded, convertView, null);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// if (view instanceof PinnedHeaderExpListView) {
		// ((PinnedHeaderExpListView)
		// view).configureHeaderView(firstVisibleItem);
		// }

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
		if (isGroupedByDate) {
			final ExpenseGroupedByDateModel expenseModel = getExpenseGroupedByDateModel(cursor);

			TextView time = (TextView) view.findViewById(R.id.time);
			time.setText(expenseModel.getTime());

			TextView amount = (TextView) view.findViewById(R.id.price);
			amount.setText(expenseModel.getAmount());

			LinearLayout categoryLayout = (LinearLayout) view.findViewById(R.id.categoryLayout);
			categoryLayout.removeAllViews();
			for (int i = 0; i < expenseModel.getCategories().size(); i++) {
				TextView category = new TextView(context);
				category.setText(expenseModel.getCategories().get(i));
				categoryLayout.addView(category, i);
			}
		} else {
			final ExpenseGroupedByCategoryNameModel expenseModel = getExpenseGroupedByCategoryNameModel(cursor);

			TextView date = (TextView) view.findViewById(R.id.time);
			date.setText(expenseModel.getDate());

			TextView amount = (TextView) view.findViewById(R.id.price);
			amount.setText(expenseModel.getAmount());

			LinearLayout categoryLayout = (LinearLayout) view.findViewById(R.id.categoryLayout);
			categoryLayout.removeAllViews();

			TextView time = new TextView(context);
			time.setText(expenseModel.getTime());
			categoryLayout.addView(time);
		}

	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
		ImageView indicator = (ImageView) view.findViewById(R.id.indicator);
		if (isExpanded) {
			indicator.setBackgroundResource(R.drawable.ic_action_expand);
		} else {
			indicator.setBackgroundResource(R.drawable.ic_action_collapse);
		}

		if (isGroupedByDate) {
			CollapsedModelGroupedByDate collapsedModel = getCollapsedByDateModel(cursor);

			TextView date = (TextView) view.findViewById(R.id.date);
			date.setText(collapsedModel.getDate());

			TextView price = (TextView) view.findViewById(R.id.price);
			price.setText(collapsedModel.getAmount());
		} else {
			CollapsedModelGroupedByCategoryName collapsedModel = getCollapsedByCategoryNameModel(cursor);

			TextView name = (TextView) view.findViewById(R.id.date);
			name.setText(collapsedModel.getName());

			TextView price = (TextView) view.findViewById(R.id.price);
			price.setText(collapsedModel.getAmount());
		}

	}

	private CollapsedModelGroupedByDate getCollapsedByDateModel(Cursor cursor) {
		CollapsedModelGroupedByDate collapsedModel = new CollapsedModelGroupedByDate();
		collapsedModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_SUM_AMOUNT)));
		collapsedModel.setDate(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_DATE)));
		return collapsedModel;
	}

	private CollapsedModelGroupedByCategoryName getCollapsedByCategoryNameModel(Cursor cursor) {
		CollapsedModelGroupedByCategoryName collapsedModel = new CollapsedModelGroupedByCategoryName();
		collapsedModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_SUM_AMOUNT)));
		collapsedModel.setName(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NAME)));
		return collapsedModel;
	}

	private ExpenseGroupedByDateModel getExpenseGroupedByDateModel(Cursor cursor) {
		ExpenseGroupedByDateModel expenseModel = new ExpenseGroupedByDateModel();
		expenseModel.setId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID)));
		expenseModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)));
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

	private ExpenseGroupedByCategoryNameModel getExpenseGroupedByCategoryNameModel(Cursor cursor) {
		ExpenseGroupedByCategoryNameModel expenseModel = new ExpenseGroupedByCategoryNameModel();
		expenseModel.setId(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_ID)));
		expenseModel.setAmount(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_AMOUNT)));
		expenseModel.setNote(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_NOTE)));
		expenseModel.setTime(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_TIME)));
		expenseModel.setDate(cursor.getLong(cursor.getColumnIndex(SQLConstants.FIELD_DATE)));

		return expenseModel;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		if (isGroupedByDate) {
			Long date = groupCursor.getLong(groupCursor.getColumnIndex(SQLConstants.FIELD_DATE));
			if (date != null) {
				return SQLDataManager.getInstance().getDayExpense(date);
			}
		} else {
			Long categoryId = groupCursor.getLong(groupCursor.getColumnIndex(SQLConstants.FIELD_ID));
			if (categoryId != null) {
				return SQLDataManager.getInstance().getCategoryExpense(categoryId);
			}
		}
		return null;
	}

	public void changeCursor(Cursor cursor, boolean isGroupedByDate) {
		this.isGroupedByDate = isGroupedByDate;
		super.changeCursor(cursor);
	}

}
