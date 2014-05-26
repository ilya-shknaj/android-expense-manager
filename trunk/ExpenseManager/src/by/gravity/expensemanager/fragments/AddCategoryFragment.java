package by.gravity.expensemanager.fragments;

import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.GlobalUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.fragments.loaders.AddCategoryLoader;
import by.gravity.expensemanager.fragments.loaders.CategoryByIDLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.fragments.loaders.UpdateCategoryLoader;
import by.gravity.expensemanager.model.CategoryModel;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.GlobalUtils;

import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;

public class AddCategoryFragment extends CommonProgressSherlockFragment {

	public static final String TAG = AddCategoryFragment.class.getSimpleName();

	private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

	private TextView categoryName;

	private View colorView;

	public static AddCategoryFragment newInstance(Integer categoryId) {

		AddCategoryFragment fragment = new AddCategoryFragment();
		Bundle bundle = new Bundle();
		if (categoryId != null) {
			bundle.putInt(ARG_CATEGORY_ID, categoryId);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	private AddCategoryFragment() {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		categoryName = (TextView) getView().findViewById(R.id.name);
		colorView = getView().findViewById(R.id.color);
		if (getCategoryId() == 0) {
			initView();
			setContentShown(true);
		}

		colorView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogHelper.showColorPickerDialog(getActivity(), (Integer) colorView.getTag(), new OnColorSelectedListener() {

					@Override
					public void onColorSelected(int color) {

						colorView.setTag(color);
						colorView.setBackgroundColor(color);
					}

				});
			}
		});

	}

	private void initBottomTabBar() {

		final View cancelButton = getView().findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GlobalUtil.hideSoftKeyboard(getActivity());
				getFragmentManager().popBackStack();
			}
		});

		final View saveButton = getView().findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GlobalUtil.hideSoftKeyboard(getActivity());

				if (getCategoryId() == 0) {
					LoaderHelper.getIntance().startLoader(AddCategoryFragment.this, LoaderHelper.ADD_CATEGORY_ID, AddCategoryFragment.this);
				} else {
					LoaderHelper.getIntance().startLoader(AddCategoryFragment.this, LoaderHelper.UPDATE_CATEGORY_ID, AddCategoryFragment.this);
				}
			}

		});
	}

	private void notifyRelatedFragmentChanges() {

		Fragment fragment = getFragmentManager().findFragmentByTag(CategoriesFragment.TAG);
		if (fragment != null) {
			CategoriesFragment categoriesFragment = (CategoriesFragment) fragment;
			categoriesFragment.notifyDataSetChanged();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		if (id == LoaderHelper.LOAD_CATEGORY_BY_ID) {
			return new CategoryByIDLoader(getActivity(), getCategoryId());
		} else if (id == LoaderHelper.ADD_CATEGORY_ID) {
			return new AddCategoryLoader(getActivity(), categoryName.getText().toString(), (Integer) colorView.getTag());
		} else if (id == LoaderHelper.UPDATE_CATEGORY_ID) {
			return new UpdateCategoryLoader(getActivity(), getCategoryId(), categoryName.getText().toString(), (Integer) colorView.getTag());
		}
		return null;
	}

	@Override
	public void onLoadFinished(int loaderId, Cursor cursor) {

		super.onLoadFinished(loaderId, cursor);
		if (loaderId == LoaderHelper.LOAD_CATEGORY_BY_ID) {
			CategoryModel categoryModel = SQLDataManager.getInstance().parseCategory(cursor);
			initView(categoryModel);
			setContentShown(true);
		} else if (loaderId == LoaderHelper.ADD_CATEGORY_ID || loaderId == LoaderHelper.UPDATE_CATEGORY_ID) {
			notifyRelatedFragmentChanges();
			((MainActivity) getActivity()).delayedPopBackStack();
		}

	}

	private void initView() {

		View categoryStatistic = getView().findViewById(R.id.categoryStatisticLayout);
		categoryStatistic.setVisibility(View.GONE);
		int color = GlobalUtils.generateRandomColor();
		colorView.setBackgroundColor(color);
		colorView.setTag(color);

		initBottomTabBar();
	}

	private void initView(CategoryModel categoryModel) {

		categoryName.setText(categoryModel.getName());

		colorView.setBackgroundColor(categoryModel.getColor());
		colorView.setTag(categoryModel.getColor());

		View categoryStatistic = getView().findViewById(R.id.categoryStatisticLayout);

		if (getCategoryId() != 0) {

			TextView lastUsageTime = (TextView) getView().findViewById(R.id.lastUsageTime);
			if (categoryModel.getLastUsageTime() != null) {
				lastUsageTime.setText(CalendarUtil.convertToFriendlyWithoutYer(categoryModel.getLastUsageTime()));
			} else {
				lastUsageTime.setText(R.string.never);
			}

			TextView countUsage = (TextView) getView().findViewById(R.id.countUsage);
			countUsage.setText(categoryModel.getCountUsage());

			TextView avgSum = (TextView) getView().findViewById(R.id.avgSum);
			avgSum.setText(StringUtil.alignToRight(categoryModel.getAvgSumString()));
			categoryStatistic.setVisibility(View.VISIBLE);
		} else {
			categoryStatistic.setVisibility(View.GONE);
		}

		initBottomTabBar();
	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {

		if (getCategoryId() != 0) {
			loaderIds.add(LoaderHelper.LOAD_CATEGORY_BY_ID);
		}
	}

	@Override
	public int getViewId() {

		return R.layout.f_add_category;
	}

	@Override
	public int getTitleResource() {

		return R.string.categories;
	}

	private Integer getCategoryId() {

		return getArguments().getInt(ARG_CATEGORY_ID);
	}

}
