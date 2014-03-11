package by.gravity.expensemanager.model;

public class PaymentMethodDetailModel {

	private String name;

	private String balance;

	private String note;

	private String currency;

	public PaymentMethodDetailModel() {

	}

	public PaymentMethodDetailModel(String name, String balance) {
		this.name = name;
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
