package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.AddCategoryFragment;
import by.gravity.expensemanager.fragments.AddPaymentFragment;
import by.gravity.expensemanager.fragments.AddPaymentMethodsFragment;
import by.gravity.expensemanager.fragments.CategoriesFragment;
import by.gravity.expensemanager.fragments.ChooseCurrencyFragment;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.ExchangeRatesFragment;
import by.gravity.expensemanager.fragments.MainFragment;
import by.gravity.expensemanager.fragments.OutcomeFragment;
import by.gravity.expensemanager.fragments.PaymentMethodsFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class MainActivity extends DrawerActivity {

	private static final Handler handler = new Handler();

	private static final int SHOW_FRAGMENT_DELAY = 250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		String action = getIntent().getAction();
		if (action.equals(Intent.ACTION_MAIN)) {
			showMainFragment();
		} else {
			enableLock();

			if (action.equals(ChoosePeriodFragment.TAG)) {
				showChoosePeriodFragment();

			} else if (action.equals(PaymentMethodsFragment.TAG)) {
				showPaymentMethodsFragment();
			} else if (action.equals(ChooseCurrencyFragment.TAG)) {
				// TODO
				showChooseCurrencyFragment(false);
			} else if (action.equals(ExchangeRatesFragment.TAG)) {
				showExchangeRatesFragment();
			} else if (action.equals(CategoriesFragment.TAG)) {
				showCategoriesFragment();
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (getIntent().getAction().equals(Intent.ACTION_MAIN)) {
			getSupportMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals(getString(R.string.action_settings))) {
			startActivity(new Intent(this, SettingActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawerItemClick(final String item) {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (item.equals(getString(R.string.outcome))) {
					showOutcomeFragment(true);
				} else if (item.equals(getString(R.string.main))) {
					showMainFragment();
				} else if (item.equals(getString(R.string.paymentsMethods))) {
					showPaymentMethodsFragment();
				}
			}
		}, SHOW_FRAGMENT_DELAY);

	}

	public void showMainFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, MainFragment.newInstance(), MainFragment.TAG).commit();

	}

	public void showOutcomeFragment(boolean isGroupedByDate) {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, OutcomeFragment.newInstance(isGroupedByDate), OutcomeFragment.TAG)
				.commit();

	}

	public void showChoosePeriodFragment() {

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.content, ChoosePeriodFragment.newInstanse());
		if (!getIntent().getAction().equals(ChoosePeriodFragment.TAG)) {
			transaction.addToBackStack(ChoosePeriodFragment.TAG);
		}
		transaction.commit();
	}

	public void showAddPaymentFragment() {

		showAddPaymentFragment(null);
	}

	public void showAddPaymentFragment(Long paymentId) {

		getSupportFragmentManager().beginTransaction().add(R.id.content, AddPaymentFragment.newInstance(paymentId))
				.addToBackStack(AddPaymentFragment.TAG).commit();
	}

	public void showPaymentMethodsFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, PaymentMethodsFragment.newInstance()).commit();
	}

	public void showAddPaymentMethodFragment() {

		showAddPaymentMethodFragment(null);
	}

	public void showAddPaymentMethodFragment(Long paymentMethodId) {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, AddPaymentMethodsFragment.newInstance(paymentMethodId))
				.addToBackStack(AddPaymentMethodsFragment.TAG).commit();
	}

	public void showSelectDateDialog(int year, int month, int day, OnDateSetListener dateSetListener) {

		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateSetListener, year, month, day, false);
		datePickerDialog.show(getSupportFragmentManager(), DatePickerDialog.class.getSimpleName());
	}

	public void showTimePickerDialog(int hourOfDay, int minute, OnTimeSetListener onTimeSetListener) {

		final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, hourOfDay, minute, true, false);
		timePickerDialog.show(getSupportFragmentManager(), TimePickerDialog.class.getSimpleName());
	}

	public void showChooseCurrencyFragment(boolean showOnlyShortCurrencies) {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, ChooseCurrencyFragment.newInstance(showOnlyShortCurrencies)).commit();
	}

	public void showExchangeRatesFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, ExchangeRatesFragment.newInstance()).commit();
	}

	public void showCategoriesFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.content, CategoriesFragment.newInstance(), CategoriesFragment.TAG).commit();
	}

	public void showAddCategoryFragment(Integer categoryID) {

		getSupportFragmentManager().beginTransaction().add(R.id.content, AddCategoryFragment.newInstance(categoryID))
				.addToBackStack(AddCategoryFragment.TAG).commit();
	}

	public void delayedPopBackStack() {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				getSupportFragmentManager().popBackStack();
			}
		}, 5);
	}

}
