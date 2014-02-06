package by.gravity.expensemanager.activity;

import android.os.Bundle;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.OutcomeFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class MainActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showCostsFragment();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDrawerItemClick(String item) {
		super.onDrawerItemClick(item);
	}

	public void showCostsFragment() {
		getSupportFragmentManager().beginTransaction().add(R.id.content, OutcomeFragment.newInstance()).commit();
		setTitle(getString(R.string.outcome));
	}

	public void showChoosePeriodFragment() {
		getSupportFragmentManager().beginTransaction().add(R.id.content, ChoosePeriodFragment.newInstanse())
				.addToBackStack(ChoosePeriodFragment.class.getSimpleName()).commit();
	}

}
