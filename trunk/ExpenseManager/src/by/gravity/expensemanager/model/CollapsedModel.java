package by.gravity.expensemanager.model;

import java.util.Calendar;

import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.util.Constants;

public class CollapsedModel {

	private String date;

	private String amount;

	private String currency;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDate(Long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		date = calendar.get(Calendar.DAY_OF_MONTH) + Constants.SPACE_STRING + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = StringUtil.convertNumberToHumanFriednly(amount);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
