package by.gravity.expensemanager.model;

import java.util.Calendar;
import java.util.Date;

import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.util.Constants;

public class ExpenseGroupedByCategoryNameModel {

	private long id;

	private String time;

	private String amount;

	private String note;

	private String date;

	public ExpenseGroupedByCategoryNameModel() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	@SuppressWarnings("deprecation")
	public void setTime(Long time) {
		Date date = new Date(time);
		int hourOfDay = date.getHours();
		int minutes = date.getMinutes();
		this.time = StringUtil.getFriendltyTime(hourOfDay, minutes);
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = StringUtil.convertNumberToHumanFriednly(amount);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDate() {
		return date;
	}

	public void setDate(Long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		date = calendar.get(Calendar.DAY_OF_MONTH) + Constants.SPACE_STRING + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

}
