package by.gravity.expensemanager.activity;

import android.os.Bundle;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.fragments.CostsFragment;

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

	private void showCostsFragment() {
		getSupportFragmentManager().beginTransaction().add(R.id.content, CostsFragment.newInstance()).commit();
		setTitle(getString(R.string.costs));
	}

}
