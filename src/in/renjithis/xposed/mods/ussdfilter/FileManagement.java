package in.renjithis.xposed.mods.ussdfilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileManagement {
	
	private static final String TAG="USSDFilter.FileManagement :";

	public static String readFileFromExternalStorage(String fileName) {
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

		if(!mExternalStorageReadable)
		{
			Log.e(TAG, "External strorage not readable");
			return null;
		}
		
		File textFile = new File(Environment.getExternalStorageDirectory(), fileName);
		if(!textFile.exists() || !textFile.canRead())
		{
			Log.e(TAG, "Unable to read file:" + textFile.getPath());
			return null;
		}
		String content = null;
		//			File file = new File(file); //for ex foo.txt
		try {
			FileReader reader = new FileReader(textFile);
			char[] chars = new char[(int) textFile.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static void writeFileToExternalStorage(String fileName, String dataString) {
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
			Log.e(TAG, "External strorage not writable");
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
}
