package in.renjithis.xposed.mods.ussdfilter;

import java.util.ArrayList;
import java.util.Arrays;

import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.FragmentActivity;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;

public class SettingsActivityOld extends Activity {
	
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
	
	
	
	
	/*
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	// enable action bar home button
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	setContentView(R.layout.settings_activity);
    	
    	// Display the fragment as the main content.
//    	if (savedInstanceState == null)
//    		getFragmentManager().beginTransaction().replace(android.R.id.content,
//    				new SettingsListFragment()).commit();
    	
    	// Find the ListView resource. 
//        ListView filterListView = (ListView) findViewById( R.id.filterListView );
//        filterListView.setItemsCanFocus(true);
//
//        // Create and populate a List of planet names.
//        String[] filterListStrings = new String[] { "Dummy" };  
//        ArrayList<String> filterList = new ArrayList<String>();
//        filterList.addAll(Arrays.asList(filterListStrings));
//        
//        ArrayAdapter<String> filterListAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, filterList);
//        filterListAdapter.add("Dummy filter");
//        filterListView.setAdapter( filterListAdapter );   
//
//        ListView othersListView = (ListView) findViewById( R.id.othersListView );
//
//        String[] othersListStrings = new String[] { "Log", "Donate", "About" };  
//        ArrayList<String> othersList = new ArrayList<String>();
//        othersList.addAll( Arrays.asList(othersListStrings) );
//        
//        // Create ArrayAdapter using the planet list.
//        ArrayAdapter<String> othersListAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, othersList);
//        
//        // Add more planets. If you passed a String[] instead of a List<String> 
//        // into the ArrayAdapter constructor, you must not add more items. 
//        // Otherwise an exception will occur.
////        listAdapter.add( "Ceres" );
////        listAdapter.add( "Pluto" );
////        listAdapter.add( "Haumea" );
////        listAdapter.add( "Makemake" );
////        listAdapter.add( "Eris" );
//        
//        // Set the ArrayAdapter as the ListView's adapter.
//        othersListView.setAdapter( othersListAdapter );   
    }
    

	
//
//    public static class PrefsFragment extends PreferenceFragment
//    implements SharedPreferences.OnSharedPreferenceChangeListener {
//    	@Override
//    	public void onCreate(Bundle savedInstanceState) {
//    		super.onCreate(savedInstanceState);
//
////    		// this is important because although the handler classes that read these settings
////    		// are in the same package, they are executed in the context of the hooked package
//    		// renjith : im not using shared preference in hooked method
////    		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
//    		addPreferencesFromResource(R.xml.preferences);
//
////    		String currentFilterString = readFile("USSDFilterString.conf");
////			EditTextPreference pref = (EditTextPreference) findPreference("pref_filterString");
////			pref.setSummary(currentFilterString);
////			pref.setText(currentFilterString);
//    		
//    		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
//    		sharedPref.registerOnSharedPreferenceChangeListener(this);
//    		
//    		onSharedPreferenceChanged(sharedPref, "pref_name");
//    	}
//
//
//    	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
////    		if (key.equals("pref_filterString")) {
////    			EditTextPreference pref = (EditTextPreference) findPreference(key);
////    			String value = sharedPreferences.getString(key, "");
////    			if (value.isEmpty()) {
////    				value = "(unchanged)";
////    				return;
////    			}    		
////    			
////    			writeFile("USSDFilterString.conf", value);
////    			pref.setSummary(value);
////    		}
//    		
//    		
//    	}
//    }
 * 
 */
}