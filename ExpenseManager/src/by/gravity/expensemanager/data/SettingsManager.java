package by.gravity.expensemanager.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import by.gravity.common.preference.PreferenceHelper;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.ContextHolder;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.PeriodDate;
import by.gravity.expensemanager.util.Constants;

public class SettingsManager extends PreferenceHelper {

	public static void putCurrentPeriod(String value) {

		putString(R.string.keyCurrentPeriod, value);
	}

	public static String getCurrentPeriod() {

		return getString(R.string.keyCurrentPeriod, R.string.defaultPeriod);
	}

	public static String getCategoriesShowCount() {

		return getString(R.string.keyCategoriesShowCountByDefault, R.string.defaultCategoriesShowCount);
	}

	public static void putCategoriesShowCount(String value) {

		putString(R.string.keyCategoriesShowCountByDefault, value);
	}

	public static String getPaymentMethod() {

		return getString(R.string.keyPaymentMethod, R.string.emptyPaymentMethods);
	}

	public static void putPaymentMethod(String paymentMethod) {

		putString(R.string.keyPaymentMethod, StringUtil.uppercaseFirstLetter(paymentMethod));
	}

	public static long getExchangeRatesLastUpdateTime() {

		return getLong(R.string.keyExchangeRates, -1);
	}

	public static String getExchangeRatesLastUpdateTimeString() {

		long lastUpdateTime = getLong(R.string.keyExchangeRates, -1);
		if (lastUpdateTime == -1) {
			return getString(R.string.never);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		return simpleDateFormat.format(new Date(lastUpdateTime));
	}

	public static void putExchangeRateLastUpdateTime(long value) {
		putLong(R.string.keyExchangeRates, value);
	}

	public static String getUsedCurrencies() {

		return getUsedCurrencies(false);
	}

	public static String getUsedCurrencies(boolean forQuery) {

		String curencies = getString(R.string.keyUsedCurrencies, R.string.keyUsedCurrencies);
		if (curencies.equals(getString(R.string.keyUsedCurrencies))) {
			String[] usedCurrencies = ContextHolder.getContext().getResources().getStringArray(R.array.defaultUseCurrencies);
			return StringUtil.strJoin(usedCurrencies, ",", forQuery);
		}
		return curencies;

	}

	public static void putUsedCurrencies(List<String> usedCurrenciesCodes) {

		putString(R.string.keyUsedCurrencies, StringUtil.strJoin(usedCurrenciesCodes.toArray(new String[] {}), ","));
	}

	public static String getUpdateCurrencyPeriod() {

		return getString(R.string.keyUpdateCurrencyPeriod, R.string.settingsPeriodEveryDay);
	}

	public static void putUpdateCurrencyPeriod(String period) {

		putString(R.string.keyUpdateCurrencyPeriod, period);
	}

	public static void putCurrentCurrency(String currency) {

		putString(R.string.keyCurrentCurrency, currency);
	}

	public static String getCurrentCurrency() {

		return getString(R.string.keyCurrentCurrency, R.string.defaultCurrencyCode);
	}

	public static String getFriendlyCurrentPeriod() {

		String currentPeriod = getCurrentPeriod();
		Calendar calendar = Calendar.getInstance();
		StringBuilder stringBuilder = new StringBuilder();
		if (currentPeriod.equals(getString(R.string.period_current_month))) {
			String dayStart = CalendarUtil.getDay(1);
			String dayEnd = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			String month = CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
			stringBuilder.append(String.format(Constants.DATE_FORMAT_WITH_START_END_DATE, dayStart, month, dayEnd, month));
		} else if (currentPeriod.equals(getString(R.string.period_prev_month))) {
			calendar.add(Calendar.MONTH, -1);
			String dayStart = CalendarUtil.getDay(1);
			String dayEnd = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			String month = CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
			stringBuilder.append(String.format(Constants.DATE_FORMAT_WITH_START_END_DATE, dayStart, month, dayEnd, month));
		} else if (currentPeriod.equals(getString(R.string.period_week))) {
			String dayEnd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String monthEnd = CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			String dayStart = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String monthStart = CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
			stringBuilder.append(String.format(Constants.DATE_FORMAT_WITH_START_END_DATE, dayStart, monthStart, dayEnd, monthEnd));
		} else if (currentPeriod.equals(getString(R.string.period_day))) {
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String month = CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
			stringBuilder.append(String.format(Constants.DATE_FORMAT_SHORT, day, month));
		} else if (currentPeriod.equals(getString(R.string.periodAllTime))) {
			stringBuilder.append(getString(R.string.periodAllTime));
		} else if (currentPeriod.equals(getString(R.string.period_user))) {
			String startDate = getUserPeriodStartDate("");
			String endDate = getUserPeriodEndDate("");

			startDate = startDate.substring(0, startDate.lastIndexOf(Constants.SPACE_STRING));
			endDate = endDate.substring(0, endDate.lastIndexOf(Constants.SPACE_STRING));
			stringBuilder.append(String.format(Constants.DATE_FORMAT_USER_PERIOD, startDate, endDate));
		}

		return stringBuilder.toString();
	}

	public static PeriodDate getCurrentPeriodDates() {

		String currentPeriod = getCurrentPeriod();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		PeriodDate periodDate = new PeriodDate();

		if (currentPeriod.equals(getString(R.string.period_current_month))) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			periodDate.setStartDate(calendar.getTime());

			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			periodDate.setEndDate(calendar.getTime());

		} else if (currentPeriod.equals(getString(R.string.period_prev_month))) {
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			periodDate.setStartDate(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			periodDate.setEndDate(calendar.getTime());

		} else if (currentPeriod.equals(getString(R.string.period_week))) {
			periodDate.setEndDate(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			periodDate.setStartDate(calendar.getTime());
		} else if (currentPeriod.equals(getString(R.string.period_day))) {
			periodDate.setEndDate(calendar.getTime());
			periodDate.setStartDate(calendar.getTime());
		} else if (currentPeriod.equals(getString(R.string.periodAllTime))) {
			calendar.set(Calendar.YEAR, 1990);
			periodDate.setStartDate(calendar.getTime());
			periodDate.setEndDate(new Date(Long.MAX_VALUE));
		} else if (currentPeriod.equals(getString(R.string.period_user))) {
			String startDate = getUserPeriodStartDate("");
			String endDate = getUserPeriodEndDate("");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
			try {
				periodDate.setStartDate(simpleDateFormat.parse(startDate));
				calendar.setTime(simpleDateFormat.parse(endDate));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				periodDate.setEndDate(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return periodDate;
	}

	public static void putUserPeriodStartDate(String value) {

		putString(R.string.keyUserPeriodStartDate, value);
	}

	public static String getUserPeriodStartDate(String defValue) {

		return getString(R.string.keyUserPeriodStartDate, defValue);
	}

	public static void putUserPeriodEndDate(String value) {

		putString(R.string.keyUserPeriodEndDate, value);
	}

	public static String getUserPeriodEndDate(String defValue) {

		return getString(R.string.keyUserPeriodEndDate, defValue);
	}

}
