package by.gravity.expensemanager.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GlobalUtils {

	public static double getRate(String from, double rateFrom, String to, double rateTo) {

		if (from.equals(to)) {
			return 1.0;
		} else if (to.equals(Constants.USD)) {
			return rateFrom;
		} else {
			return 1.0 / rateFrom * rateTo;
		}
	}

	public static String getFormattedRate(String from, double rateFrom, String to, double rateTo) {

		NumberFormat format = new DecimalFormat("#.#######");
		return format.format(getRate(from, rateFrom, to, rateTo));
	}
}
