package by.gravity.expensemanager.model;

public class MenuModel {

	private int drawableId;

	private String text;

	private boolean itemSelected;

	public MenuModel(int drawableId, String text) {
		this.drawableId = drawableId;
		this.text = text;

	}

	public MenuModel(int drawableId, String text, boolean itemSelected) {
		this(drawableId, text);
		this.itemSelected = itemSelected;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public String getText() {
		return text;
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

}
