package com.liueq.testdagger.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.liueq.testdagger.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liueq on 29/7/15.
 */
public class SharedPreferenceRepoImpl implements SharedPreferenceRepo {

    private Context mContext;

    public SharedPreferenceRepoImpl(Context context){
        mContext = context;
    }

    @Override
    public void saveProperties(HashMap<String, String> map) {
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for(String key : map.keySet()){
            editor.putString(key, map.get(key));
        }

        editor.commit();
    }

    @Override
    public HashMap<String, String> getAllProperties() {
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);

        HashMap<String, String> map = new HashMap<>();
        Map<String, ?> sp_map = sp.getAll();

        for(String key : sp_map.keySet()){
            String.valueOf(sp_map.get(key));
            map.put(key, String.valueOf(sp_map.get(key)));
        }

        return map;
    }

    @Override
    public String getProterties(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);

        return sp.getString(key, null);
    }
}
