package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.ChooseCurrencyFragment;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.PaymentMethodsFragment;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.onEditCompleteListener;

public class SettingActivity extends PreferenceActivity {

	private static final int SELECT_PERIOD_REQUEST_CODE = 1;

	private static final int SELECT_PAYMENT_METHOD_REQUEST_CODE = 2;

	private static final int SELECT_CURRENCY_REQUEST_CODE = 3;

	private Preference currentPeriodPreference;

	private Preference categoriesShowCount;

	private Preference defaultCurrency;

	private Preference exchangeRates;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		currentPeriodPreference = findPreference(getString(R.string.keyCurrentPeriod));
		currentPeriodPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.setAction(ChoosePeriodFragment.class.getSimpleName());
				startActivityForResult(intent, SELECT_PERIOD_REQUEST_CODE);
				return false;
			}
		});
		updateCurrentPeriodSummary();

		categoriesShowCount = findPreference(getString(R.string.keyCategoriesShowCountByDefault));
		categoriesShowCount.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				DialogHelper.showNumberEditDialog(SettingActivity.this, R.string.settingCategoriesCount, 0, SettingsManager.getCategoriesShowCount(),
						new onEditCompleteListener() {

							@Override
							public void onEditCompelted(String text) {
								SettingsManager.putCategoriesShowCount(text);
								updateCategoriesShowCountSummary();
							}
						});
				return false;
			}
		});
		updateCategoriesShowCountSummary();

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

		exchangeRates = findPreference(getString(R.string.keyExchangeRates));
		exchangeRates.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		updateExchangeRateSummary();

		defaultCurrency = findPreference(getString(R.string.keyDefautCurrency));
		defaultCurrency.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.setAction(ChooseCurrencyFragment.class.getSimpleName());
				startActivityForResult(intent, SELECT_CURRENCY_REQUEST_CODE);
				return false;
			}
		});
		updateDefaultCurrency();

	}

	private void updatePaymentMethodSummary() {
		categoriesShowCount.setSummary(SettingsManager.getPaymentMethod());
	}

	private void updateCategoriesShowCountSummary() {
		categoriesShowCount.setSummary(SettingsManager.getCategoriesShowCount());
	}

	private void updateCurrentPeriodSummary() {
		currentPeriodPreference.setSummary(SettingsManager.getCurrentPeriod());
	}

	private void updateExchangeRateSummary() {
		exchangeRates.setSummary(getString(R.string.lastTimeUpdate) + Constants.NEW_STRING + SettingsManager.getExchangeRatesLastUpdateTime());
	}

	private void updateDefaultCurrency() {
		defaultCurrency.setSummary(SettingsManager.getDefaultCurrency());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case SELECT_PAYMENT_METHOD_REQUEST_CODE:
			SettingsManager.putPaymentMethod(data.getAction());
			updatePaymentMethodSummary();
			break;

		case SELECT_PERIOD_REQUEST_CODE:
			updateCurrentPeriodSummary();
			break;
			
		case SELECT_CURRENCY_REQUEST_CODE:
			updateDefaultCurrency();
			break;
		default:
			break;
		}

	}

}
