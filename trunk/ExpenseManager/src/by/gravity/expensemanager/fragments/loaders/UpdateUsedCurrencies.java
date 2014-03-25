package by.gravity.expensemanager.fragments.loaders;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import by.gravity.expensemanager.data.SQLDataManager;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.data.helper.SQLConstants;

public class UpdateUsedCurrencies extends CursorLoader {

	private long[] usedIds;

	public UpdateUsedCurrencies(Context context, long[] usedIds) {

		super(context);
		this.usedIds = usedIds;
	}

	@Override
	public Cursor loadInBackground() {

		SQLDataManager.getInstance().updateUsedCurrencies(usedIds);
		updateSettings();
		return null;
	}

	private void updateSettings() {

		Cursor cursor = SQLDataManager.getInstance().getCurrenciesShortCursor();
		List<String> usedCurrencies = new ArrayList<String>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			usedCurrencies.add(cursor.getString(cursor.getColumnIndex(SQLConstants.FIELD_CODE)));
		}
		SettingsManager.putUsedCurrencies(usedCurrencies);
	}

}
