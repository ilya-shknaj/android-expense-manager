package by.gravity.expensemanager.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import by.gravity.expensemanager.data.helper.SQLConstants;
import by.gravity.expensemanager.model.RateModel;
import by.gravity.expensemanager.util.Constants;

public class FinanceDataManager {

	private static FinanceDataManager instance;

	private static final String BASE_URL = "http://query.yahooapis.com/v1/public/yql?q=%s&env=store://datatables.org/alltableswithkeys&format=json";

	private static final String QUERY_URL = "select * from yahoo.finance.xchange where pair in (%s)";

	private static final String USD = "USD";

	private static final String CURRENCY_FORMAT = "\"%s%s\"";

	private static final String QUERY = "query";

	private static final String RESULTS = "results";

	private static final String RATE_ARRAY = "rate";

	private static final String RATE_VALUE = "Rate";

	private static final String ID = "id";

	public static FinanceDataManager getInstance() {

		if (instance == null) {
			instance = new FinanceDataManager();
		}

		return instance;
	}

	private FinanceDataManager() {

	}

	public String getUrl() {

		Cursor cursor = SQLDataManager.getInstance().getCurrenciesFullCursor();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (i != 0) {
				builder.append(Constants.COMMA_STRING);
			}
			builder.append(String.format(CURRENCY_FORMAT, USD, cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE))));
		}
		try {
			return String.format(BASE_URL, URLEncoder.encode(String.format(QUERY_URL, builder.toString()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<RateModel> getRates(String responce) {

		List<RateModel> rateList = new ArrayList<RateModel>();

		try {
			JSONObject jsonObject = new JSONObject(responce);
			if (jsonObject.has(QUERY)) {
				JSONObject query = jsonObject.getJSONObject(QUERY);
				if (query.has(RESULTS)) {
					JSONObject result = query.getJSONObject(RESULTS);
					if (result.has(RATE_ARRAY)) {
						JSONArray rates = result.getJSONArray(RATE_ARRAY);
						for (int i = 0; i < rates.length(); i++) {
							JSONObject rateObject = rates.getJSONObject(i);
							if (rateObject.getString(ID).length() == 6) {
								RateModel rateModel = new RateModel();
								if (rateObject.has(RATE_VALUE)) {
									rateModel.setRate(rateObject.getDouble(RATE_VALUE));
								}
								rateModel.setCode(rateObject.getString(ID).substring(3));
								rateList.add(rateModel);
							}
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rateList;
	}

}
