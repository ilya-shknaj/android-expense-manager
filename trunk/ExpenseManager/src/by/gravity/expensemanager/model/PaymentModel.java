package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentModel {

	private String balance;

	private List<PaymentDetail> listPaymensDetail;

	public PaymentModel() {
		listPaymensDetail = new ArrayList<PaymentDetail>();
	}

	public List<PaymentDetail> getListPaymensDetail() {
		return listPaymensDetail;
	}

	public void setListPaymensDetail(List<PaymentDetail> listPaymensDetail) {
		this.listPaymensDetail = listPaymensDetail;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
