package by.gravity.expensemanager.fragments.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.model.RateModel;

public class UpdateRateLoader extends CursorLoader {

	private RateModel rateModel;

	public UpdateRateLoader(Context context, String code, double rate) {

		super(context);
		rateModel = new RateModel();
		rateModel.setCode(code);
		rateModel.setRate(rate);
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().updateRate(rateModel);
		return null;
	}

}
