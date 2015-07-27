package com.liueq.testdagger.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.data.model.Account;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 14/7/15.
 */
public class JsonParser {
    public final static String TAG = "JsonParser";

    /**
     * obj list 转Json数组
     * @param list
     * @return
     */
    public static String objToJson(List list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "objToJson after convert, json is " + json);
        }
        return json;
    }

    /**
     * json数组转obj
     * @param json
     * @return
     */
    public static List<Account> jsonToObj(JsonReader json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Account>>(){}.getType();
        List<Account> obj = gson.fromJson(json, collectionType);

        if(BuildConfig.DEBUG){
            Log.d(TAG, "jsonToObj after convert, obj is " + obj.toString());
            Log.d(TAG, "jsonToObj obj name is " + obj.getClass().getName());
        }

        return obj;
    }

}