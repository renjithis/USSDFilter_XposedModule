package in.renjithis.xposed.mods.ussdfilter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import android.os.Environment;
import android.widget.Toast;

// Imports for XposedBridge
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

//Imports for PhoneUtils class
import android.content.Context;

public class USSDFilter implements IXposedHookLoadPackage {

	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		//        XposedBridge.log("Loaded app: " + lpparam.packageName);

		if(!lpparam.packageName.equals("com.android.phone"))
			return;

		XposedBridge.log("Found phone app");

		findAndHookMethod("com.android.phone.PhoneUtils", 
				lpparam.classLoader, 
				"displayMMIComplete", 
				"com.android.internal.telephony.Phone",
				"android.content.Context",
				"com.android.internal.telephony.MmiCode",
				"android.os.Message",
				"android.app.AlertDialog",
				new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				// this will be called before the clock was updated by the original method
				XposedBridge.log("beforeHookedMethod displayMMIComplete");

				Context context=(Context) param.args[1];
				Object mmiCode=param.args[2];
				Method getMessageMethod=mmiCode.getClass().getDeclaredMethod("getMessage");

				// get this from user. need to change hardcoding
				
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
					XposedBridge.log("External strorage not readable");
					return;
				}
				File filterStringFile = new File(Environment.getExternalStorageDirectory(), "USSDFilterString.conf");
				if(!filterStringFile.exists() || !filterStringFile.canRead())
				{
					XposedBridge.log("Unable to read file:" + filterStringFile.getPath());
					return;
				}

				String filterString= readFile(filterStringFile); // need to remove trailing newline if exists
				XposedBridge.log("filterString="+filterString);
				String text = (String) getMessageMethod.invoke(mmiCode);
				XposedBridge.log("text="+text);
				
				if(text.contains(filterString))
				{
					// need to add more functionality, like logging, etc

					XposedBridge.log("Text contains filterString");
					Toast.makeText(context, text, Toast.LENGTH_LONG).show();
					// This prevents the actual hooked method from being called
					param.setResult(mmiCode);
				}
			}
		});
	}
	
	public String readFile(File file)
	{
		String content = null;
		//			File file = new File(file); //for ex foo.txt
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
