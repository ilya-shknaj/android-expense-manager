package by.gravity.expensemanager.model;

import by.gravity.common.utils.StringUtil;

public class PaymentMethodModel {

	private Long id;

	private String name;

	private String balance;

	private String note;

	private String currency;

	public PaymentMethodModel() {

	}

	public PaymentMethodModel(String name, String balance) {

		this.name = name;
		this.balance = balance;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
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

		this.balance = StringUtil.convertNumberToHumanFriednly(balance);
	}

	public String getCurrency() {

		return currency;
	}

	public void setCurrency(String currency) {

		this.currency = currency;
	}

}
