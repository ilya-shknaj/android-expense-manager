package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

public class GroupPriceModel {

	private String groupName;

	private String groupPrice;

	private List<PriceModel> priceList;

	public GroupPriceModel() {
		priceList = new ArrayList<PriceModel>();
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
		this.groupPrice = groupPrice;
	}

	public List<PriceModel> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<PriceModel> priceList) {
		this.priceList = priceList;
	}

}
