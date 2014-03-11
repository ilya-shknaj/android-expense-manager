package by.gravity.expensemanager.fragments.loaders;

import java.util.HashMap;

public class LoaderHelper {

	public static final int OUTCOME_GROUP_BY_DATE_ID = -2;

	public static final int OUTCOME_GROUP_BY_CATEGORY_NAME_ID = -3;

	public static final int ADD_PAYMENT_CURRENCIES_ID = -4;

	public static final int ADD_PAYMENT_PAYMENT_METHODS_ID = -5;

	public static final int ADD_PAYMENT_CATEGORIES_ID = -6;

	public static final int ADD_PAYMENT_EXPENSE_ID = -7;

	public static final int GET_PAYMENT_METHODS_ID = -8;

	public static final int ADD_PAYMENT_METHOD_CURENCIES_ID = -9;
	
	public static final int PAYMENT_METHOD_BY_ID = -10;

	public static final String ARG_EXPENSE_DATA = "ARG_EXPENSE_DATA";

	public static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

	private static LoaderHelper instance;

	private HashMap<String, HashMap<Integer, LoaderStatus>> loaderStatusMap = new HashMap<String, HashMap<Integer, LoaderStatus>>();

	public enum LoaderStatus {
		NOT_STARTED, STARTED, FINISHED;
	}

	public static LoaderHelper getIntance() {
		if (instance == null) {
			instance = new LoaderHelper();
		}
		return instance;
	}

	public void putLoaderStatus(String fragmentName, int loaderId, LoaderStatus status) {
		if (loaderStatusMap.get(fragmentName) == null) {
			loaderStatusMap.put(fragmentName, new HashMap<Integer, LoaderStatus>());
		}
		loaderStatusMap.get(fragmentName).put(loaderId, status);
	}

	public LoaderStatus getLoaderStatus(String fragmentName, int loaderId) {
		if (loaderStatusMap.get(fragmentName) == null || loaderStatusMap.get(fragmentName).get(loaderId) == null) {
			return LoaderStatus.NOT_STARTED;
		}
		return loaderStatusMap.get(fragmentName).get(loaderId);
	}

	public void clearLoaderStatus(String fragmentName) {
		if (loaderStatusMap.get(fragmentName) != null) {
			loaderStatusMap.put(fragmentName, null);
		}
	}
}
