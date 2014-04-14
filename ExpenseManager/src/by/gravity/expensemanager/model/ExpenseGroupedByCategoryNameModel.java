package by.gravity.expensemanager.model;

import java.util.Calendar;

import by.gravity.common.utils.CalendarUtil;
import by.gravity.expensemanager.util.Constants;

public class ExpenseGroupedByCategoryNameModel extends CommonExpenseModel {

	private String date;

	private String paymentMethod;

	public ExpenseGroupedByCategoryNameModel() {

	}

	public String getDate() {

		return date;
	}

	public void setDate(Long time) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		date = calendar.get(Calendar.DAY_OF_MONTH) + Constants.SPACE_STRING + CalendarUtil.getMonth(calendar.get(Calendar.MONTH));
	}

	public String getPaymentMethod() {

		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {

		this.paymentMethod = paymentMethod;
	}

}
