package by.gravity.expensemanager.model;

public class MenuModel {

	private int drawableId;

	private String text;

	public MenuModel(int drawableId, String text) {
		this.drawableId = drawableId;
		this.text = text;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public String getText() {
		return text;
	}

}
