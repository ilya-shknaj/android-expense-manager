package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

import by.gravity.common.utils.StringUtil;

public class GroupPriceModel {

	private String groupName;

	private String groupPrice;

	private List<ExpenseModel> priceList;

	public GroupPriceModel() {
		priceList = new ArrayList<ExpenseModel>();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupPrice() {
		return groupPrice;
	}

	public void setGroupPrice(String groupPrice) {
		this.groupPrice = StringUtil.convertNumberToHumanFriednly(groupPrice);
	}

	public List<ExpenseModel> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ExpenseModel> priceList) {
		this.priceList = priceList;
	}

}
