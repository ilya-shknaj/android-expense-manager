package by.gravity.expensemanager.model;

public class CurrencyModel {

	private String code;

	private String name;

	private String symbol;

	public CurrencyModel(String code, String name, String symbol) {
		this.code = code;
		this.name = name;
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

}
