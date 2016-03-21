package com.liueq.testdagger.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by liueq on 17/2/2016.
 * BackUp Database
 */
public class BackUpTool {

	/**
	 * Check if the import target can be opened
	 * @param password
	 * @param path
	 * @return
	 */
	public static boolean canOpenDB(String password, String path){
		SQLiteDatabase database = null;
		try{
			database = SQLiteDatabase.openDatabase(path, password, null, 0);
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}

		if(database != null){
			database.close();
			return true;
		}else{
			return false;
		}
	}

	public static void importDB(Context context, String input, String import_password){
		//Close current db
		SQLCipherOpenHelper helper = SQLCipherOpenHelper.getInstance(context);
		String db_path = helper.getDatabase().getPath();
		helper.closeDatabase();

		//Copy file
		File input_f = new File(input);
		File output_f = new File(db_path);
		output_f.delete();
		output_f = new File(db_path);

		copyFile(input_f, output_f);

		//Change password
		SQLiteDatabase database = null;
		try{
			database = SQLiteDatabase.openDatabase(db_path, import_password, null, 0);
			database.rawExecSQL("pragma rekey=\"" + TestApplication.getDBPassword() + "\";");
			if(database != null){
				database.close();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String exportDB(Context context){
		SQLCipherOpenHelper helper = SQLCipherOpenHelper.getInstance(context);
		String db_path = helper.getDatabase().getPath();

		String timestamp = String.valueOf(System.currentTimeMillis());
		String output = Environment.getExternalStorageDirectory() + "/encrypted" + GoldenHammer.timeFormatB(timestamp) + ".db";
		File file = new File(db_path);
		File file_out = new File(output);
		if(file.exists()){
			try {
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(file_out);

				byte [] tmp = new byte[1024];

				while(fis.read(tmp) > 0){
					fos.write(tmp);
				}

				fos.flush();
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return output;
	}

	private static void copyFile(File a, File b){
		if(a.exists()){
			try {
				FileInputStream fis = new FileInputStream(a);
				FileOutputStream fos = new FileOutputStream(b);

				byte [] tmp = new byte[1024];

				while(fis.read(tmp) > 0){
					fos.write(tmp);
				}

				fos.flush();
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
