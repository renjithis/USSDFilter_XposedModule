package in.renjithis.xposed.mods.ussdfilter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.graphics.drawable.BitmapDrawable;
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
		XposedBridge.log(TAG + ": " + logString);
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
				// this will be called before the USSD message is displayed by the original method
				myLog("beforeHookedMethod displayMMIComplete");

				Context context = (Context) param.args[1];
				Object mmiCode = param.args[2];
				Method getMessageMethod = mmiCode.getClass().getDeclaredMethod("getMessage");
				Method isUssdRequestMethod = mmiCode.getClass().getDeclaredMethod("isUssdRequest");
		
				if((Boolean)isUssdRequestMethod.invoke(mmiCode))
				{
					myLog("USSD Request detected. Not filtering");
					return;
				}
					
				ArrayList<Filter> filterSettings = filterSettings();
				
				for (Filter filter : filterSettings) {
					if(!filter.enabled || filter.subStringRegEx == null) {
						myLog("Filter is not enabled. Filer name : " + filter.name);
						continue;
					}

                    myLog("Filter ="+filter.name+" type="+filter.type+" subStringRegEx="+filter.subStringRegEx+" outputType="+filter.outputType);
					String mmiText = (String) getMessageMethod.invoke(mmiCode);
					myLog("mmiText="+mmiText);
					
					Boolean filterMatch = Boolean.FALSE;
					
					if(filter.type == FilterType.TYPE_ALL)
						filterMatch = Boolean.TRUE;
					else if(filter.type == FilterType.TYPE_SUBSTRING) {
						if(mmiText.contains(filter.subStringRegEx) || mmiText.contains(filter.subStringRegEx.subSequence(0, filter.subStringRegEx.length() - 1))) {
                            filterMatch = Boolean.TRUE;
                            myLog("Filter matched");
                        }
                        else
                            myLog("Does not contain substring");
					}
					else if(filter.type == FilterType.TYPE_REGEX) {
//						myLog("RegEx matching not yet implemented");
                        if(mmiText.matches(filter.subStringRegEx))
                            filterMatch = Boolean.TRUE;
					}
                    else
                        myLog("Did not match filter");

                    String logString="\n";

                    if(filterMatch)
                    {
						// need to add more functionality, like logging, etc

						myLog("Text contains filterString");
						if(filter.outputType == OutputType.TYPE_TOAST)
                        {
                            myLog("Showing Toast");
							Toast.makeText(context, mmiText, Toast.LENGTH_LONG).show();
                            logString += "[Toast] ";
                        }
						else if(filter.outputType == OutputType.TYPE_NOTIFICATION)
                        {
                            myLog("Showing Notification");
							showNotification(context, "USSD Message Received", mmiText);
                            logString += "[Notification] ";
                        }
                        else
                        {
                            myLog("Not showing anything (Silent)");
                            logString += "[Silent] ";
                        }
                        logString += mmiText;
						// This prevents the actual hooked method from being called
						param.setResult(mmiCode);
					}
                    else
                    {
                        logString += "[Allowed] " + mmiText;
                    }

                    myLog("Writing to log. Text=" + logString);
                    FileManagement.writeFileToExternalStorage("USSDFilter.log", logString, Boolean.TRUE);
				}
			}
		});
	}
	
	private ArrayList<Filter> filterSettings() {
		ArrayList<Filter> filterList = new ArrayList<Filter>();
		
		// dummy data - replace with DB calls **************************
		Filter filter = new Filter();
		filter.name = "Filter1";
		filter.type= FilterType.TYPE_SUBSTRING;
		filter.subStringRegEx = FileManagement.readFileFromExternalStorage("USSDFilterString.conf");
		filter.outputType = OutputType.TYPE_NOTIFICATION;
		filter.priority = 1;
		filter.enabled = Boolean.TRUE;
		
		filterList.add(filter);
		
		//**************************************************************
		
		
		return filterList;
	}

	private void showNotification(Context context, String title, String contentText) {
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
//                .setLargeIcon(((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap())
		        .setContentTitle(title)
		        .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setAutoCancel(true);
        myLog("Initialised notification builder");
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myLog("Initialised notification manager");
        Notification notification = nBuilder.build();
        myLog("Notification built");
        mNotificationManager.notify(0, notification);
	}
	 
}
