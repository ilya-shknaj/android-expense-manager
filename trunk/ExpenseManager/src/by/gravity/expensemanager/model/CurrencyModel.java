package by.gravity.expensemanager.model;

public class CurrencyModel {

	private String code;

	private String name;

	public CurrencyModel() {

	}

	public CurrencyModel(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
