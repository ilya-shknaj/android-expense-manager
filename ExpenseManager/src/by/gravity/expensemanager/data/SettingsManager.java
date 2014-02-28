package by.gravity.expensemanager.data;

import java.util.Calendar;

import by.gravity.common.preference.PreferenceHelper;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.util.Constants;

public class SettingsManager extends PreferenceHelper {

	private static int DEFAULT_PERIOD = R.string.period_current_month;

	public static void putCurrentPeriod(String value) {
		putString(R.string.keyCurrentPeriod, value);
	}

	public static String getCurrentPeriod() {
		return getString(R.string.keyCurrentPeriod, DEFAULT_PERIOD);
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
