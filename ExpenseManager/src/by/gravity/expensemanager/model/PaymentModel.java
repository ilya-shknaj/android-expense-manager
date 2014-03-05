package by.gravity.expensemanager.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentModel {

	private String balance;

	private List<PaymentMethodDetailModel> listPaymensDetail;

	public PaymentModel() {
		listPaymensDetail = new ArrayList<PaymentMethodDetailModel>();
	}

	public List<PaymentMethodDetailModel> getListPaymensDetail() {
		return listPaymensDetail;
	}

	public void setListPaymensDetail(List<PaymentMethodDetailModel> listPaymensDetail) {
		this.listPaymensDetail = listPaymensDetail;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
