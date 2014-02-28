package by.gravity.expensemanager.model;

import java.util.Calendar;

import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.util.Constants;

public class CollapsedModelGroupedByDate {

	private String date;

	private String amount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDate(Long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		date = CalendarUtil.getDay(calendar.get(Calendar.DAY_OF_MONTH)) + Constants.SPACE_STRING + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		int index = amount.lastIndexOf(Constants.NEW_STRING);
		this.amount = StringUtil.convertNumberToHumanFriednly(amount.substring(0, index));
	}

}
