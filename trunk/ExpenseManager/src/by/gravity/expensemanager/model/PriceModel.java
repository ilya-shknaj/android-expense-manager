package by.gravity.expensemanager.model;

import by.gravity.common.utils.StringUtil;

public class PriceModel {

	private String date;

	private String price;

	private String[] category;

	public PriceModel() {

	}

	public PriceModel(String date, String price, String... category) {
		this.date = date;
		this.price = price;
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = StringUtil.convertNumberToHumanFriednly(price);
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}

}
