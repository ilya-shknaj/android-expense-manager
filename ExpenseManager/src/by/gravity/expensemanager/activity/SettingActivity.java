package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.fragments.CategoriesFragment;
import by.gravity.expensemanager.fragments.ChooseCurrencyFragment;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.ExchangeRatesFragment;
import by.gravity.expensemanager.fragments.PaymentMethodsFragment;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.DialogHelper;
import by.gravity.expensemanager.util.DialogHelper.OnSelectedListener;
import by.gravity.expensemanager.util.DialogHelper.onEditCompleteListener;

public class SettingActivity extends PreferenceActivity {

	private static final int SELECT_PERIOD_REQUEST_CODE = 1;

	private static final int SELECT_PAYMENT_METHOD_REQUEST_CODE = 2;

	private static final int SELECT_CURRENCY_REQUEST_CODE = 3;

	private static final int EXCHANGE_RATE_REQUEST_CODE = 4;

	private Preference currentPeriodPreference;

	private Preference categoriesShowCount;

	private Preference exchangeRates;

	private Preference usedCurrencies;

	private Preference paymentMethod;

	private Preference updateCurrencyPeriod;

	private Preference categories;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		currentPeriodPreference = findPreference(getString(R.string.keyCurrentPeriod));
		currentPeriodPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				startForResult(ChoosePeriodFragment.TAG, SELECT_PERIOD_REQUEST_CODE);
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
							public void onEditCompelted(double value) {

								SettingsManager.putCategoriesShowCount(String.valueOf((int) value));
								updateCategoriesShowCountSummary();
							}
						});
				return false;
			}
		});
		updateCategoriesShowCountSummary();

		paymentMethod = findPreference(getString(R.string.keyPaymentMethod));
		paymentMethod.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				startForResult(PaymentMethodsFragment.TAG, SELECT_PAYMENT_METHOD_REQUEST_CODE);
				return false;
			}
		});

		exchangeRates = findPreference(getString(R.string.keyExchangeRates));
		exchangeRates.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				startForResult(ExchangeRatesFragment.TAG, EXCHANGE_RATE_REQUEST_CODE);
				return false;
			}
		});
		updateExchangeRateSummary();

		usedCurrencies = findPreference(getString(R.string.keyUsedCurrencies));
		usedCurrencies.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				startForResult(ChooseCurrencyFragment.TAG, SELECT_CURRENCY_REQUEST_CODE);
				return false;
			}
		});
		updateUsedCurrencies();

		updateCurrencyPeriod = findPreference(getString(R.string.keyUpdateCurrencyPeriod));
		updateCurrencyPeriod.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				DialogHelper.showRadioButtonDialog(SettingActivity.this, R.string.settingsUpdateCurrencyPeriod, new String[] {
						getString(R.string.settingsPeriodEveryDay), getString(R.string.settingsPeriodManualy) },
						SettingsManager.getUpdateCurrencyPeriod(), new OnSelectedListener() {

							@Override
							public void onSelectedListener(String value) {

								SettingsManager.putUpdateCurrencyPeriod(value);
								updateCurrencyPeriod();
							}
						});

				return false;
			}
		});
		updateCurrencyPeriod();

		categories = findPreference(getString(R.string.keyCategories));
		categories.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				startForResult(CategoriesFragment.TAG, 0);
				return false;
			}
		});

	}

	private void startForResult(String action, int requestCode) {

		Intent intent = new Intent(SettingActivity.this, MainActivity.class);
		intent.setAction(action);
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onResume() {

		super.onResume();
		updatePaymentMethodSummary();
	}

	private void updatePaymentMethodSummary() {

		paymentMethod.setSummary(SettingsManager.getPaymentMethod());
	}

	private void updateCategoriesShowCountSummary() {

		categoriesShowCount.setSummary(SettingsManager.getCategoriesShowCount());
	}

	private void updateCurrentPeriodSummary() {

		currentPeriodPreference.setSummary(SettingsManager.getCurrentPeriod());
	}

	private void updateExchangeRateSummary() {

		exchangeRates.setSummary(getString(R.string.settingsLastTimeUpdate) + Constants.NEW_STRING
				+ SettingsManager.getExchangeRatesLastUpdateTimeString());
	}

	private void updateUsedCurrencies() {

		usedCurrencies.setSummary(SettingsManager.getUsedCurrencies());
	}

	private void updateCurrencyPeriod() {

		updateCurrencyPeriod.setSummary(SettingsManager.getUpdateCurrencyPeriod());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			switch (requestCode) {
			case EXCHANGE_RATE_REQUEST_CODE:
				updateExchangeRateSummary();
				break;
			}

			return;
		}

		switch (requestCode) {
		case SELECT_PAYMENT_METHOD_REQUEST_CODE:
			updatePaymentMethodSummary();
			break;

		case SELECT_PERIOD_REQUEST_CODE:
			updateCurrentPeriodSummary();
			break;

		case SELECT_CURRENCY_REQUEST_CODE:
			updateUsedCurrencies();
			break;

		default:
			break;
		}

	}
}
