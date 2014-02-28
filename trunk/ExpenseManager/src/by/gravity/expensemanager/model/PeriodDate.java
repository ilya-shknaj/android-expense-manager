package by.gravity.expensemanager.model;

import java.util.Date;

public class PeriodDate {
	private String startDate;

	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = String.valueOf(startDate.getTime());
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = String.valueOf(endDate.getTime());
	}
}
