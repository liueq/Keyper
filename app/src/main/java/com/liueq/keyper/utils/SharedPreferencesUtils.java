package com.liueq.keyper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.liueq.keyper.TestApplication;

import java.util.HashMap;

/**
 * Created by liueq on 24/4/2016.
 * SharedPreferencesUtils
 * Simplify SharedPreferences used.
 */
public class SharedPreferencesUtils {

	public static void set(String property_name, String key, String value){
		SharedPreferences sp = TestApplication.getApplication().getSharedPreferences(property_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void set(String property_name, HashMap<String, String> map){
		SharedPreferences sp = TestApplication.getApplication().getSharedPreferences(property_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		for(String key : map.keySet()){
			editor.putString(key, map.get(key));
		}

		editor.commit();
	}

	public static String get(String property_name, String key){
		SharedPreferences sp = TestApplication.getApplication().getSharedPreferences(property_name, Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
}
