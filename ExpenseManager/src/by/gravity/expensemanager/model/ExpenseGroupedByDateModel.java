package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseGroupedByDateModel extends CommonExpenseModel {

	private String paymentMethod;

	private List<String> categories;

	public ExpenseGroupedByDateModel() {

		categories = new ArrayList<String>();
	}

	public List<String> getCategories() {

		return categories;
	}

	public void setCategories(List<String> categories) {

		this.categories = categories;
	}

	public String getPaymentMethod() {

		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {

		this.paymentMethod = paymentMethod;
	}

}
