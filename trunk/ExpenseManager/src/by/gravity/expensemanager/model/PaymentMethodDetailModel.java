package by.gravity.expensemanager.model;

public class PaymentMethodDetailModel {

	private String name;

	private String balance;

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

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
