package by.gravity.expensemanager.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import android.graphics.Color;

public class GlobalUtils {

	private static NumberFormat exchangeNumberFormat;

	private static Random random;

	public static NumberFormat getExchangeNumberFormat() {

		if (exchangeNumberFormat == null) {
			exchangeNumberFormat = new DecimalFormat("#.#######");
		}

		return exchangeNumberFormat;
	}

	public static double getRate(String from, double rateFrom, String to, double rateTo) {

		if (from.equals(to)) {
			return 1.0;
		} else if (to.equals(Constants.USD)) {
			return rateFrom;
		} else {
			return 1.0 / rateTo * rateFrom;
		}
	}

	public static double convertToUsdRate(String from, double selectedCurrencyRate, double newRate) {

		if (from.equals(Constants.USD)) {
			return newRate;
		} else {
			return 1 / (newRate / selectedCurrencyRate);
		}
	}

	public static String getFormattedRate(String from, double rateFrom, String to, double rateTo) {

		return getExchangeNumberFormat().format(getRate(from, rateFrom, to, rateTo));
	}

	public static int generateRandomColor() {

		if (random == null) {
			random = new Random();
		}
		return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
}
