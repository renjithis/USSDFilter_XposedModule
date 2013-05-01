package in.renjithis.xposed.mods.ussdfilter;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

//Imports for PhoneUtils class. Need to cleanup and remove those which are not needed


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
//import android.bluetooth.IBluetoothHeadsetPhone;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.net.sip.SipManager;
//import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
//import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

//import com.android.internal.telephony.Call;
//import com.android.internal.telephony.CallManager;
//import com.android.internal.telephony.CallStateException;
//import com.android.internal.telephony.CallerInfo;
//import com.android.internal.telephony.CallerInfoAsyncQuery;
//import com.android.internal.telephony.Connection;
//import com.android.internal.telephony.MmiCode;
//import com.android.internal.telephony.Phone;
//import com.android.internal.telephony.PhoneConstants;
//import com.android.internal.telephony.TelephonyCapabilities;
//import com.android.internal.telephony.TelephonyProperties;
//import com.android.internal.telephony.cdma.CdmaConnection;
//import com.android.internal.telephony.sip.SipPhone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import java.lang.reflect.Method;

public class USSDFilter implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("Loaded app: " + lpparam.packageName);
        
        if(!lpparam.packageName.equals("com.android.phone"))
        	return;
        
        XposedBridge.log("Found phone app");
        
        //todo
        
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
//		    			Class MmiCode = mmiCode.getClass();
		    			XposedBridge.log("mmicode type = "+mmiCode.toString());
		    			
		    			String filterString="free GPRS";
		    			
		    			Method getMessageMethod=mmiCode.getClass().getDeclaredMethod("getMessage", null);
//		    			String text = context.getText(R.string.puk_unlocked);
		    			String text = (String) getMessageMethod.invoke(mmiCode, null);
		    			XposedBridge.log("text="+text);
		    			if(text.contains(filterString))
		    			{
		    				XposedBridge.log("Text contains filterString");
			    			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			    			param.setResult(mmiCode);
		    			}
		    		}
		    		@Override
		    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		    			// this will be called after the clock was updated by the original method
		    			XposedBridge.log("afterHookedMethod displayMMIComplete");
		    		}
			});
    }
    
}
