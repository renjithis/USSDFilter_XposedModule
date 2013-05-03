package in.renjithis.xposed.mods.ussdfilter;

import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.content.SharedPreferences;
import android.app.Activity;
import android.widget.Toast;
import android.os.Bundle;

public class PreferencesActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	// Display the fragment as the main content.
    	if (savedInstanceState == null)
    		getFragmentManager().beginTransaction().replace(android.R.id.content,
    				new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);

    		// this is important because although the handler classes that read these settings
    		// are in the same package, they are executed in the context of the hooked package
    		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
    		addPreferencesFromResource(R.xml.preferences);

    		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
    		sharedPref.registerOnSharedPreferenceChangeListener(this);
    		
    		onSharedPreferenceChanged(sharedPref, "pref_name");
    	}

    	@Override
    	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    		
    		
    	}
    }
    
    
}