package by.gravity.expensemanager.fragments.loaders;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.common.http.HttpClient;
import by.gravity.expensemanager.data.FinanceDataManager;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.model.RateModel;

public class RefreshRatesLoader extends CursorLoader {

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
			e.printStackTrace();
		}

		return null;
	}

}
