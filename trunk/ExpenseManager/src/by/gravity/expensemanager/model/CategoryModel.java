package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryModel {

	private String name;

	private int color;

	private String countUsage;

	private Date lastUsageTime;

	private List<String> avgSum = new ArrayList<String>();

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public int getColor() {

		return color;
	}

	public void setColor(int color) {

		this.color = color;
	}

	public Date getLastUsageTime() {

		return lastUsageTime;
	}

	public void setLastUsageTime(Date lastUsageTime) {

		this.lastUsageTime = lastUsageTime;
	}

	public String getCountUsage() {

		return countUsage;
	}

	public void setCountUsage(String countUsage) {

		this.countUsage = countUsage;
	}

	public List<String> getAvgSum() {

		return avgSum;
	}

	public String getAvgSumString() {

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < avgSum.size(); i++) {
			builder.append(avgSum.get(i));
			if (i + 1 != avgSum.size()) {
				builder.append("\n");
			}
		}

		return builder.toString();
	}
}
