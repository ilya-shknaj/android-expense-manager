package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.onEditCompleteListener;

public class SettingActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		Preference currentPeriodPreference = findPreference(getString(R.string.keyCurrentPeriod));
		currentPeriodPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.setAction(ChoosePeriodFragment.class.getSimpleName());
				startActivityForResult(intent, 22);
				return false;
			}
		});
		currentPeriodPreference.setSummary(SettingsManager.getCurrentPeriod());

		final Preference categoriesShowCount = findPreference(getString(R.string.keyCategoriesShowCountByDefault));
		categoriesShowCount.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				DialogHelper.showNumberEditDialog(SettingActivity.this, R.string.categoriesCount, 0, SettingsManager.getCategoriesShowCount(),
						new onEditCompleteListener() {

							@Override
							public void onEditCompelted(String text) {
								SettingsManager.putCategoriesShowCount(text);
								categoriesShowCount.setSummary(text);
							}
						});
				return false;
			}
		});
		categoriesShowCount.setSummary(SettingsManager.getCategoriesShowCount());
	}

}
