package by.gravity.expensemanager.fragments;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.loaders.CategoriesSumAmountLoader;
import by.gravity.expensemanager.fragments.loaders.LoaderHelper;
import by.gravity.expensemanager.model.CategoryModel;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class PieGraphFragment extends CommonProgressSherlockFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void initView(List<CategoryModel> categoryModels) {
		if (categoryModels.size() != 0) {
			initCategoiresGraph(categoryModels);
			setContentShown(true);
		} else {
			setContentEmpty(true);
		}

		TextView staticticDate = (TextView) getView().findViewById(R.id.staticticDate);
		staticticDate.setText(SettingsManager.getFriendlyCurrentPeriod());

	}

	private void initCategoiresGraph(List<CategoryModel> categoryModels) {
		List<PieSlice> pieSlices = new ArrayList<PieSlice>();

		for (int i = 0; i < categoryModels.size(); i++) {
			PieSlice pieSlice = new PieSlice();
			pieSlice.setTitle(categoryModels.get(i).getName());
			pieSlice.setColor(categoryModels.get(i).getColor());
			pieSlice.setValue(categoryModels.get(i).getSumAmount());
			pieSlices.add(pieSlice);
		}

		PieGraph graph = (PieGraph) getView().findViewById(R.id.piegraph);
		graph.setSlices(pieSlices);

		ViewGroup categoriesListLayout = (ViewGroup) getView().findViewById(R.id.categoriesList);
		categoriesListLayout.removeAllViews();
		for (int i = 0; i < categoryModels.size(); i++) {
			View categoryView = LayoutInflater.from(getActivity()).inflate(R.layout.i_graph_category, null);

			ImageView image = (ImageView) categoryView.findViewById(R.id.image);
			image.setBackgroundColor(categoryModels.get(i).getColor());

			TextView name = (TextView) categoryView.findViewById(R.id.name);
			name.setText(categoryModels.get(i).getName());

			TextView amount = (TextView) categoryView.findViewById(R.id.amount);
			amount.setText(String.valueOf(StringUtil.convertNumberToHumanFriednly(categoryModels.get(i).getSumAmount()) + " "
					+ categoryModels.get(i).getCurrency()));
			categoriesListLayout.addView(categoryView);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		if (id == LoaderHelper.CATEGORIES_SUM_AMOUNT_ID) {
			return new CategoriesSumAmountLoader(getActivity());
		}
		return null;
	}

	@Override
	public void onLoadFinished(int loaderId, Cursor cursor) {
		super.onLoadFinished(loaderId, cursor);
		if (loaderId == LoaderHelper.CATEGORIES_SUM_AMOUNT_ID) {
			initView(SQLDataManager.getInstance().parseCategoiesSumAmount(cursor));
		}
	}

	@Override
	public int getViewId() {
		return R.layout.f_pie_graph;
	}

	@Override
	public int getTitleResource() {
		return 0;
	}

	@Override
	public void getLoaderIds(List<Integer> loaderIds) {
		loaderIds.add(LoaderHelper.CATEGORIES_SUM_AMOUNT_ID);
	}

}
