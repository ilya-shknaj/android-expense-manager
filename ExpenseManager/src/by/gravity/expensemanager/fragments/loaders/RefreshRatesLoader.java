package by.gravity.expensemanager.fragments.loaders;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import by.gravity.common.http.HttpClient;
import by.gravity.expensemanager.data.FinanceDataManager;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.model.RateModel;
import by.gravity.expensemanager.util.EmptyCursor;

public class RefreshRatesLoader extends CursorLoader {

	private static final String TAG = RefreshRatesLoader.class.getSimpleName();

	public RefreshRatesLoader(Context context) {

		super(context);
	}

	@Override
	public Cursor loadInBackground() {

		String url = FinanceDataManager.getInstance().getUrl();
		try {
			String response = HttpClient.get(getContext()).loadAsString(url);
			List<RateModel> rateList = FinanceDataManager.getInstance().getRates(response);
			SQLDataManager.getInstance().updateRates(rateList);
			SettingsManager.putExchangeRateLastUpdateTime(System.currentTimeMillis());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return new EmptyCursor();
		}

		return null;
	}

}
