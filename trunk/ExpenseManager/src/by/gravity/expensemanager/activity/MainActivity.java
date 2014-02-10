package by.gravity.expensemanager.activity;

import android.os.Bundle;
import android.os.Handler;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.ChoosePeriodFragment;
import by.gravity.expensemanager.fragments.MainFragment;
import by.gravity.expensemanager.fragments.OutcomeFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class MainActivity extends DrawerActivity {

	private static final Handler handler = new Handler();

	private static final int SHOW_FRAGMENT_DELAY = 250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showMainFragment();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDrawerItemClick(String item) {
		if (item.equals(getString(R.string.outcome))) {
			showOutcomeFragmentDelayed();
		} else if (item.equals(getString(R.string.main))) {
			showMainFragment();
		}
	}

	private void showOutcomeFragmentDelayed() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				showOutcomeFragment();
			}
		}, SHOW_FRAGMENT_DELAY);
	}

	public void showMainFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, MainFragment.newInstance()).commit();

	}

	public void showOutcomeFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, OutcomeFragment.newInstance()).commit();

	}

	public void showChoosePeriodFragment() {
		getSupportFragmentManager().beginTransaction().add(R.id.content, ChoosePeriodFragment.newInstanse())
				.addToBackStack(ChoosePeriodFragment.class.getSimpleName()).commit();
	}

}
