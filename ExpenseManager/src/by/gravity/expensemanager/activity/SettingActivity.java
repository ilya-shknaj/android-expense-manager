package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.PaymentMethodsFragment;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.onEditCompleteListener;

public class SettingActivity extends PreferenceActivity {

	private static final int SELECT_PERIOD_REQUEST_CODE = 1;

	private static final int SELECT_PAYMENT_METHOD_REQUEST_CODE = 2;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		final Preference currentPeriodPreference = findPreference(getString(R.string.keyCurrentPeriod));
		currentPeriodPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.setAction(ChoosePeriodFragment.class.getSimpleName());
				startActivityForResult(intent, SELECT_PERIOD_REQUEST_CODE);
				return false;
			}
		});
		currentPeriodPreference.setSummary(SettingsManager.getCurrentPeriod());

		final Preference categoriesShowCount = findPreference(getString(R.string.keyCategoriesShowCountByDefault));
		categoriesShowCount.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				DialogHelper.showNumberEditDialog(SettingActivity.this, R.string.settingCategoriesCount, 0, SettingsManager.getCategoriesShowCount(),
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

		final Preference paymentMethod = findPreference(getString(R.string.keyPaymentMethod));
		paymentMethod.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.setAction(PaymentMethodsFragment.class.getSimpleName());
				startActivityForResult(intent, SELECT_PAYMENT_METHOD_REQUEST_CODE);
				return false;
			}
		});
		paymentMethod.setSummary(SettingsManager.getPaymentMethod());

		final Preference exchangeRates = findPreference(getString(R.string.keyExchangeRates));
		exchangeRates.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void setPaymentMethodSummary(String text) {
		final Preference paymentMethod = findPreference(getString(R.string.keyPaymentMethod));
		paymentMethod.setSummary(text);
	}

	@SuppressWarnings("deprecation")
	private void setCurrentPerdiodSummary(String text) {
		final Preference currentPeriodPreference = findPreference(getString(R.string.keyCurrentPeriod));
		currentPeriodPreference.setSummary(SettingsManager.getCurrentPeriod());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case SELECT_PAYMENT_METHOD_REQUEST_CODE:
			SettingsManager.putPaymentMethod(data.getAction());
			setPaymentMethodSummary(data.getAction());
			break;

		case SELECT_PERIOD_REQUEST_CODE:
			setCurrentPerdiodSummary(SettingsManager.getCurrentPeriod());
			break;
		default:
			break;
		}

	}

}
