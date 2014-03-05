package by.gravity.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.AddPaymentFragment;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.MainFragment;
import by.gravity.expensemanager.fragments.OutcomeFragment;
import by.gravity.expensemanager.fragments.PaymentMethodsFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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
			// showMainFragment();
			 showOutcomeFragment(true);
//			showPaymentMethodsFragment();
		} else if (action.equals(ChoosePeriodFragment.class.getSimpleName())) {
			showChoosePeriodFragment();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
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
	public void onDrawerItemClick(String item) {
		if (item.equals(getString(R.string.outcome))) {
			showOutcomeFragmentDelayed();
		} else if (item.equals(getString(R.string.main))) {
			showMainFragment();
		} else if (item.equals(getString(R.string.paymentsMethods))) {
			showPaymentMethodsFragment();
		}
	}

	private void showOutcomeFragmentDelayed() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				showOutcomeFragment(true);
			}
		}, SHOW_FRAGMENT_DELAY);
	}

	public void showMainFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, MainFragment.newInstance()).commit();

	}

	public void showOutcomeFragment(boolean isGroupedByDate) {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, OutcomeFragment.newInstance(isGroupedByDate)).commit();

	}

	public void showChoosePeriodFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, ChoosePeriodFragment.newInstanse())
				.addToBackStack(ChoosePeriodFragment.class.getSimpleName()).commit();
	}

	public void showAddPaymentFragment() {
		showAddPaymentFragment(null);
	}

	public void showAddPaymentFragment(Long paymentId) {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, AddPaymentFragment.newInstance(paymentId))
				.addToBackStack(AddPaymentFragment.class.getSimpleName()).commit();
	}

	public void showPaymentMethodsFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, PaymentMethodsFragment.newInstance()).commit();
	}

	public void showSelectDateDialog(int year, int month, int day, OnDateSetListener dateSetListener) {
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(dateSetListener, year, month, day, false);
		datePickerDialog.show(getSupportFragmentManager(), DatePickerDialog.class.getSimpleName());
	}

	public void showTimePickerDialog(int hourOfDay, int minute, OnTimeSetListener onTimeSetListener) {
		final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, hourOfDay, minute, true, false);
		timePickerDialog.show(getSupportFragmentManager(), TimePickerDialog.class.getSimpleName());
	}

}
