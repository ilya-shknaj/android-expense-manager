package by.gravity.expensemanager.model;

import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.util.Constants;

public class CollapsedModelGroupedByCategoryName {

	private String name;

	private String amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		int index = amount.lastIndexOf(Constants.NEW_STRING);
		this.amount = StringUtil.convertNumberToHumanFriednly(amount.substring(0, index));
	}

}
