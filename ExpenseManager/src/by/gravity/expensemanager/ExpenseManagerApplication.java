package by.gravity.expensemanager;

import java.util.ArrayList;
import java.util.List;

import by.gravity.common.CoreApplication;
import by.gravity.expensemanager.api.YahooFinanceApi;

public class ExpenseManagerApplication extends CoreApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		List<String> test = new ArrayList<String>();
		test.add("EUR");
		test.add("BYR");
		YahooFinanceApi.getUrl(test);
	}

}
