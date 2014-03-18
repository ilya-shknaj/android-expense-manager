package by.gravity.expensemanager.api;

import java.util.List;

public class YahooFinanceApi {

	private static final String BASE_URL = "http://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.xchange where pair in (%s)&env=store://datatables.org/alltableswithkeys&format=json";

	private static final String USD = "USD";

	private static final String FORMAT = "\"%s%s\"";

	public static String getUrl(List<String> codeList) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codeList.size(); i++) {
			builder.append(String.format(FORMAT, USD, codeList.get(i)));
			if (i + 1 < codeList.size()) {
				builder.append(",");
			}
		}

		return String.format(BASE_URL, builder.toString());
	}

}
