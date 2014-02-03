package by.gravity.expensemanager.model;

public class Model extends ExpandableModel {

	public Model() {

	}

	public Model(String date, String price) {
		this.date = date;
		this.price = price;
	}

	private String date;

	private String price;

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
		this.price = price;
	}

}
