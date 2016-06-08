package com.liueq.keyper.utils;

import android.os.Environment;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.keyper.BuildConfig;
import com.liueq.keyper.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by liueq on 14/7/15.
 * FileReader (not use)
 */
public class FileReader {

    public final static String TAG = "FileReader";

    private static FileReader instance = new FileReader();

    private String mFilePath;
    private String mFileName;

    private FileReader() {
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFileName = Constants.STORAGE_FILE;
    }

    public static FileReader getInstance(){
        return instance;
    }

    public JsonReader retrieveData() {

        File file = null;
        File dir = null;
        StringBuffer sb = new StringBuffer();
        java.io.FileReader fileReader = null;

        if(Environment.isExternalStorageRemovable() == false){
            file = new File(mFilePath, mFileName);
            dir = new File(mFilePath);

            if(!dir.exists()){
                dir.mkdirs();
            }

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
        File file = null;
        File dir = null;
        FileWriter fileWriter = null;

        if(Environment.isExternalStorageRemovable() == false) {
            file = new File(mFilePath, mFileName);
            dir = new File(mFilePath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(data);
                fileWriter.flush();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        Log.d(TAG, "Set file path = " + mFilePath);
        this.mFilePath = mFilePath;
    }

    public String getmFilName() {
        return mFileName;
    }

    public void setmFilName(String mFilName) {
        this.mFileName = mFilName;
    }
}
