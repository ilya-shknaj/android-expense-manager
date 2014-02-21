package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.gravity.common.utils.StringUtil;

public class ExpenseModel {

	private long id;

	private String currency;

	private String time;

	private String amount;

	private String note;

	private List<String> categories;

	public ExpenseModel() {
		categories = new ArrayList<String>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
