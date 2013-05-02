package in.renjithis.xposed.mods.ussdfilter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationManager;

// Imports for XposedBridge
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

//Imports for PhoneUtils class
import android.content.Context;

public class USSDFilter implements IXposedHookLoadPackage {

	private String TAG="USSDFilter";
	
	private void myLog(String logString) {
		XposedBridge.log(TAG + " :" + logString);
	}
	
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		//        XposedBridge.log("Loaded app: " + lpparam.packageName);

		if(!lpparam.packageName.equals("com.android.phone"))
			return;

		myLog("Found phone app");

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
				myLog("beforeHookedMethod displayMMIComplete");

				Context context=(Context) param.args[1];
				Object mmiCode=param.args[2];
				Method getMessageMethod=mmiCode.getClass().getDeclaredMethod("getMessage");

				// get this from user. need to change file based method to user preference

				String filterString = readFile("USSDFilterString.conf"); // need to remove trailing newline if exists
				if(filterString == null)
				{
					myLog("filterString is null");
					return;
				}
				filterString = filterString.trim();
				myLog("filterString="+filterString);
				
				String mmiText = (String) getMessageMethod.invoke(mmiCode);
				myLog("mmiText="+mmiText);
				
				if(mmiText.contains(filterString) || mmiText == "")
				{
					// need to add more functionality, like logging, etc

					myLog("Text contains filterString");
					Toast.makeText(context, mmiText, Toast.LENGTH_LONG).show();
					showNotification(context, "USSD Message Received", mmiText);
					
					
					// This prevents the actual hooked method from being called
					param.setResult(mmiCode);
				}
			}
		});
	}
	
	private String readFile(String fileName)
	{
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
			myLog("External strorage not readable");
			return null;
		}
		
		File textFile = new File(Environment.getExternalStorageDirectory(), fileName);
		if(!textFile.exists() || !textFile.canRead())
		{
			myLog("Unable to read file:" + textFile.getPath());
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
	
	
	private void showNotification(Context context, String title, String contentText) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(contentText);
		mBuilder.setAutoCancel(true);
		

		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
		
		
		// Alternate implementation
		
//		// Build notification
//		Notification notification = new Notification.Builder(context)
//		        .setContentTitle("New mail from " + "test@gmail.com")
//		        .setContentText("Subject")
//		        .setSmallIcon(R.drawable.icon);
//		
//		// Hide the notification after its selected
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		        
//		mNotificationManager.notify(0, notification); 
	}
	 
}
