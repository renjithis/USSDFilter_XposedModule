package in.renjithis.xposed.mods.ussdfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a list of Filters. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link FilterDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link FilterListFragment} and the item details (if present) is a
 * {@link FilterDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link FilterListFragment.Callbacks} interface to listen for item selections.
 */
public class SettingsActivity extends FragmentActivity implements
		FilterListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preference_menu, menu);
		
		return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Toast.makeText(this, "Add selected. not yet implemented", Toast.LENGTH_SHORT).show();
			
//			  Intent intent = new Intent(this, FilterPropertiesActivity.class);
//			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			  startActivity(intent);
			
			break;
		case android.R.id.home:
			Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
			finish();
			break;
			
		default:
			break;
		}

		return true;
	} 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list);

		// enable action bar home button
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
		if (findViewById(R.id.filter_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((FilterListFragment) getSupportFragmentManager().findFragmentById(
					R.id.filter_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link FilterListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(FilterDetailFragment.ARG_ITEM_ID, id);
			FilterDetailFragment fragment = new FilterDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.filter_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, FilterDetailActivity.class);
			detailIntent.putExtra(FilterDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
