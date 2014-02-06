package by.gravity.expensemanager.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import by.gravity.common.utils.ContextHolder;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.adapter.MenuAdapter;
import by.gravity.expensemanager.model.MenuModel;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class DrawerActivity extends SherlockFragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView listView;

	private ActionBar mActionBar;

	private SherlockActionBarDrawerToggle mDrawerToggle;

	private static final List<MenuModel> DRAWER_MENU = getDrawerMenu();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.a_main);
		initDrawer();
	}

	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.leftDrawer);

		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		MenuAdapter adapter = new MenuAdapter(this, R.layout.i_menu, DRAWER_MENU);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);

		mDrawerToggle = new SherlockActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_light, 0, 0);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MenuModel selectedItem = DRAWER_MENU.get(position);
			mActionBar.setTitle(selectedItem.getText());
			mDrawerLayout.closeDrawer(listView);
			onDrawerItemClick(selectedItem.getText());
		}
	}

	public void onDrawerItemClick(String item) {

	}

	/**
	 * A drawer listener can be used to respond to drawer events such as
	 * becoming fully opened or closed. You should always prefer to perform
	 * expensive operations such as drastic relayout when no animation is
	 * currently in progress, either before or after the drawer animates.
	 * 
	 * When using ActionBarDrawerToggle, all DrawerLayout listener methods
	 * should be forwarded if the ActionBarDrawerToggle is not used as the
	 * DrawerLayout listener directly.
	 */
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	private static List<MenuModel> getDrawerMenu() {
		List<MenuModel> drawerMenu = new ArrayList<MenuModel>();
		Context context = ContextHolder.getContext();
		drawerMenu.add(new MenuModel(R.drawable.ic_menu_outcome, context.getString(R.string.outcome)));
		drawerMenu.add(new MenuModel(R.drawable.ic_menu_income, context.getString(R.string.income)));

		return drawerMenu;
	}

}
