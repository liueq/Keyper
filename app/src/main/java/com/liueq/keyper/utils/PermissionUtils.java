package com.liueq.keyper.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.liueq.keyper.TestApplication;

import java.util.HashMap;

/**
 * Created by liueq on 25/4/2016.
 * Permission Utils
 */
public class PermissionUtils {

	private static HashMap<String, Integer> mPermissionToRequestCode;

	private final static String [] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
	private final static int [] mRequestCodes = {2333};

	static {
		mPermissionToRequestCode = new HashMap<String, Integer>();
		for(int i = 0; i < mPermissions.length; i++){
			mPermissionToRequestCode.put(mPermissions[i], mRequestCodes[i]);
		}
	}

	public static boolean hasPermission(String permission){
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
			return true;
		}

		int has_permission = TestApplication.getApplication().checkSelfPermission(permission);
		if(has_permission != PackageManager.PERMISSION_GRANTED){
			return false;
		}else{
			return true;
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	public static boolean requestPermission(Activity activity, final String permission){
		if(hasPermission(permission)){
			return false;
		}else if(mPermissionToRequestCode.get(permission) == null){
			return false;
		}

		String []permissions = new String[]{permission};
		activity.requestPermissions(permissions, mPermissionToRequestCode.get(permission));

		return true;
	}

	public static int getRequestCode(String permission){
		if(mPermissionToRequestCode.get(permission) == null){
			return 0;
		}else {
			return mPermissionToRequestCode.get(permission);
		}
	}

}
