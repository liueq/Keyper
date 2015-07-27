package com.liueq.testdagger.utils;

import android.os.Environment;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by liueq on 14/7/15.
 */
public class FileReader {

    public final static String TAG = "FileReader";

    private static FileReader instance = new FileReader();

    private FileReader() {
    }

    public static FileReader getInstance(){
        return instance;
    }

    public JsonReader retrieveData() {
        File file = null;
        StringBuffer sb = new StringBuffer();
        java.io.FileReader fileReader = null;

        if(Environment.isExternalStorageRemovable() == false){
            file = new File(Environment.getExternalStorageDirectory() + Constants.STORAGE_FILE);
            if(!file.exists()){
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "retrieveData File do not exist.");
                }

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("[]");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            fileReader = new java.io.FileReader(file);
            JsonReader jsonReader = new JsonReader(fileReader);
            jsonReader.setLenient(true);
            if(BuildConfig.DEBUG) {
                Log.d(TAG, "readFile is " + sb.toString());
            }
            return jsonReader;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean presistData(String data){
        File file = new File(Environment.getExternalStorageDirectory(), "password.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
