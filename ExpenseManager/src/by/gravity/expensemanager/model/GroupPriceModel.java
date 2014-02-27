package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

import by.gravity.common.utils.StringUtil;

public class GroupPriceModel {

	private String groupName;

	private String groupPrice;

	private List<ExpenseGroupedByDateModel> priceList;

	public GroupPriceModel() {
		priceList = new ArrayList<ExpenseGroupedByDateModel>();
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

	public List<ExpenseGroupedByDateModel> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ExpenseGroupedByDateModel> priceList) {
		this.priceList = priceList;
	}

}
