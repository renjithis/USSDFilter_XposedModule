package in.renjithis.xposed.mods.ussdfilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.content.SharedPreferences;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;

public class PreferencesActivity extends Activity {
	
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
			Toast.makeText(this, "Menu Add selected", Toast.LENGTH_SHORT).show();
			
//			  Intent intent = new Intent(this, AddFilterActivity.class);
//			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			  startActivity(intent);
			
			break;
		case android.R.id.home:
//			Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
			finish();
			break;
			
		default:
			break;
		}

		return true;
	} 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	// enable action bar home button
    	getActionBar().setDisplayHomeAsUpEnabled(true);

    	
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

//    		// this is important because although the handler classes that read these settings
//    		// are in the same package, they are executed in the context of the hooked package
    		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
    		addPreferencesFromResource(R.xml.preferences);

    		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
    		sharedPref.registerOnSharedPreferenceChangeListener(this);
    		
    		onSharedPreferenceChanged(sharedPref, "pref_name");
    	}


    	// not working . SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
    	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    		myLog(key);
    		if (key.equals("pref_filterString")) {
    			EditTextPreference pref = (EditTextPreference) findPreference(key);
    			String value = sharedPreferences.getString(key, "");
    			myLog(pref.getText());
    			if (value.isEmpty()) {
    				value = "(unchanged)";
    				return;
    			}
    			
    			myLog(value);
    		
    			
    			writeFile("USSDFilterString.conf", value);
    			
    			pref.setSummary(value);
    		}
    	}
    
    	private void writeFile(String fileName, String dataString) {
    		// check if external storage (sdcard/user accessible internal storage) is avaiable
    		boolean mExternalStorageAvailable = false;
    		boolean mExternalStorageReadable = false;
    		boolean mExternalStorageWriteable = false;
    		String state = Environment.getExternalStorageState();

    		if (Environment.MEDIA_MOUNTED.equals(state)) {
    			// We can read and write the media
    			mExternalStorageAvailable = mExternalStorageReadable = mExternalStorageWriteable = true;
    		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    			// We can only read the media
    			mExternalStorageAvailable = true;
    			mExternalStorageReadable = true;
    			mExternalStorageWriteable = false;
    		} else {
    			// Something else is wrong. It may be one of many other states, but all we need
    			//  to know is we can neither read nor write
    			mExternalStorageAvailable = mExternalStorageReadable = mExternalStorageWriteable = false;
    		}

    		if(!mExternalStorageWriteable)
    		{
    			myLog("External strorage not writable");
    			return;
    		}
    		
    		File textFile = new File(Environment.getExternalStorageDirectory(), fileName);
    		
    		
    		try {
    			// if file doesnt exists, then create it
    			if (!textFile.exists()) {
    				textFile.createNewFile();
    			}

    			FileWriter fw = new FileWriter(textFile.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(dataString);
    			bw.close();		
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	private void myLog(String string) {
    		Logger logger=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    		logger.log(new LogRecord(Level.WARNING, string));
    	}
    	
    }
    
	

	
}